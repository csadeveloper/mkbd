WITH BASE_ACCOUNTS AS (SELECT ACCOUNT, 1 AS CURRENCY
                       FROM DENPASAR.LEDGER_HISTORICAL@cammon
                       WHERE TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                         AND TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
                         AND GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                         AND ACCOUNT LIKE DECODE(NULL, NULL, ACCOUNT, NULL || '%')
                       GROUP BY ACCOUNT),
     ACCOUNT_CATEGORIES AS (SELECT BA.ACCOUNT,
                                   BA.CURRENCY,
                                   CASE
                                       WHEN AT.CATEGORY IN (1, 4) THEN 0
                                       WHEN AT.CATEGORY IN (2, 3, 5) THEN 1
                                       ELSE NULL END AS SUBACCOUNT_TYPE
                            FROM BASE_ACCOUNTS BA
                                     LEFT JOIN DENPASAR.ACCOUNT A ON BA.ACCOUNT = A.CODE
                                     LEFT JOIN DENPASAR.ACCOUNT_TYPE AT ON A.ACCOUNT_TYPE = AT.ID AND AT.ID < 500),
     ACCOUNT_WITH_CLIENTS AS (SELECT AC.ACCOUNT,
                                     AC.CURRENCY,
                                     CASE WHEN VC.BUY IS NOT NULL THEN 9 ELSE AC.SUBACCOUNT_TYPE END AS SUBACCOUNT_TYPE
                              FROM ACCOUNT_CATEGORIES AC
                                       LEFT JOIN DENPASAR.VIEWCLIENTTYPE VC ON AC.ACCOUNT = VC.BUY AND VC.TYPE <> 11),
     ACCOUNT_WITH_FLAGS AS (SELECT AWC.ACCOUNT,
                                   AWC.CURRENCY,
                                   AWC.SUBACCOUNT_TYPE,
                                   CASE WHEN A.CODE IS NOT NULL THEN 1 ELSE 0 END AS TRANS_FLAG
                            FROM ACCOUNT_WITH_CLIENTS AWC
                                     LEFT JOIN DENPASAR.ACCOUNT A ON AWC.ACCOUNT = A.CODE AND A.ACCOUNT_TYPE = 109),
     OPENING_BALANCES_01 AS (SELECT L.ACCOUNT,
                                    AWF.SUBACCOUNT_TYPE,
                                    CASE WHEN AWF.SUBACCOUNT_TYPE = 0 THEN SUM(L.AMOUNTIDR) ELSE 0 END  AS OPENBAL_DEBIT,
                                    CASE WHEN AWF.SUBACCOUNT_TYPE = 1 THEN -SUM(L.AMOUNTIDR) ELSE 0 END AS OPENBAL_CREDIT
                             FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                      JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                             WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                               AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                               AND L.TRANS_CURRENT = 0
                               AND AWF.SUBACCOUNT_TYPE IN (0, 1)
                             GROUP BY L.ACCOUNT, AWF.SUBACCOUNT_TYPE),
     OPENING_BALANCES_9 AS (SELECT ACCOUNT,
                                   SUM(CASE WHEN SUBACCOUNT_TOTAL > 0 THEN SUBACCOUNT_TOTAL ELSE 0 END)  AS OPENBAL_DEBIT,
                                   SUM(CASE WHEN SUBACCOUNT_TOTAL < 0 THEN -SUBACCOUNT_TOTAL ELSE 0 END) AS OPENBAL_CREDIT
                            FROM (SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR) AS SUBACCOUNT_TOTAL
                                  FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                           JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                                  WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                                    AND L.TRANS_CURRENT = 0
                                    AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                    AND AWF.SUBACCOUNT_TYPE = 9
                                  GROUP BY L.ACCOUNT, L.SUBACCOUNT)
                            GROUP BY ACCOUNT),
     CURRENT_DEBITS AS (SELECT L.ACCOUNT, SUM(L.AMOUNTIDR) AS DEBIT
                        FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                 JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                        WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                          AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                          AND L.TRANS_CURRENT = 1
                          AND L.AMOUNTIDR >= 0.0
                          AND ((AWF.SUBACCOUNT_TYPE IN (0, 1) AND AWF.TRANS_FLAG = 0 AND
                                L.TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd'))
                            OR (AWF.TRANS_FLAG = 1 AND L.TRANS_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd'))
                            OR (AWF.SUBACCOUNT_TYPE = 9 AND L.TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd')))
                        GROUP BY L.ACCOUNT),
     CURRENT_CREDITS AS (SELECT L.ACCOUNT, SUM(-L.AMOUNTIDR) AS CREDIT
                         FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                  JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                         WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                           AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                           AND L.TRANS_CURRENT = 1
                           AND L.AMOUNTIDR < 0.0
                           AND ((AWF.SUBACCOUNT_TYPE IN (0, 1) AND AWF.TRANS_FLAG = 0 AND
                                 L.TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd'))
                             OR (AWF.TRANS_FLAG = 1 AND L.TRANS_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd'))
                             OR (AWF.SUBACCOUNT_TYPE = 9 AND L.TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd')))
                         GROUP BY L.ACCOUNT),
     CLIENT_ENDING_ADJUSTMENTS AS (SELECT ACCOUNT,
                                          SUM(CASE WHEN SUBACCOUNT_TOTAL > 0 THEN SUBACCOUNT_TOTAL ELSE 0 END)  AS ENDBAL_DEBIT_ADJ,
                                          SUM(CASE WHEN SUBACCOUNT_TOTAL < 0 THEN -SUBACCOUNT_TOTAL ELSE 0 END) AS ENDBAL_CREDIT_ADJ
                                   FROM (SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR) AS SUBACCOUNT_TOTAL
                                         FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                                  JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                                         WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
                                           AND L.TRANS_DATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                           AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                           AND AWF.SUBACCOUNT_TYPE = 9
                                         GROUP BY L.ACCOUNT, L.SUBACCOUNT
                                         HAVING SUM(L.AMOUNTIDR) <> 0.0)
                                   GROUP BY ACCOUNT),
     RAW_BALANCES AS (SELECT AWF.ACCOUNT,
                             AWF.CURRENCY,
                             AWF.SUBACCOUNT_TYPE,
                             AWF.TRANS_FLAG,
                             NVL(OB01.OPENBAL_DEBIT, 0) + NVL(OB9.OPENBAL_DEBIT, 0)   AS OPENBAL_DEBIT,
                             NVL(OB01.OPENBAL_CREDIT, 0) + NVL(OB9.OPENBAL_CREDIT, 0) AS OPENBAL_CREDIT,
                             NVL(CD.DEBIT, 0)                                         AS DEBIT,
                             NVL(CC.CREDIT, 0)                                        AS CREDIT,
                             CASE
                                 WHEN AWF.SUBACCOUNT_TYPE = 0 THEN
                                     NVL(OB01.OPENBAL_DEBIT, 0) + NVL(CD.DEBIT, 0) - NVL(CC.CREDIT, 0)
                                 WHEN AWF.SUBACCOUNT_TYPE = 9 THEN
                                     NVL(OB9.OPENBAL_DEBIT, 0) + NVL(CEA.ENDBAL_DEBIT_ADJ, 0)
                                 ELSE 0
                                 END                                                  AS RAW_ENDBAL_DEBIT,
                             CASE
                                 WHEN AWF.SUBACCOUNT_TYPE = 1 THEN
                                     NVL(OB01.OPENBAL_CREDIT, 0) + NVL(CC.CREDIT, 0) - NVL(CD.DEBIT, 0)
                                 WHEN AWF.SUBACCOUNT_TYPE = 9 THEN
                                     NVL(OB9.OPENBAL_CREDIT, 0) + NVL(CEA.ENDBAL_CREDIT_ADJ, 0)
                                 ELSE 0
                                 END                                                  AS RAW_ENDBAL_CREDIT
                      FROM ACCOUNT_WITH_FLAGS AWF
                               LEFT JOIN OPENING_BALANCES_01 OB01 ON AWF.ACCOUNT = OB01.ACCOUNT
                               LEFT JOIN OPENING_BALANCES_9 OB9 ON AWF.ACCOUNT = OB9.ACCOUNT
                               LEFT JOIN CURRENT_DEBITS CD ON AWF.ACCOUNT = CD.ACCOUNT
                               LEFT JOIN CURRENT_CREDITS CC ON AWF.ACCOUNT = CC.ACCOUNT
                               LEFT JOIN CLIENT_ENDING_ADJUSTMENTS CEA ON AWF.ACCOUNT = CEA.ACCOUNT)

SELECT
    A.VD_SEKURITAS_CAT, A.VD_LINE_NO, A.ACCOUNT, B.NAME, A.AMT
FROM (
    SELECT B.VD_SEKURITAS_CAT, B.VD_LINE_NO, A.ACCOUNT, A.AMT
FROM (SELECT ACCOUNT, SUM(AMT) AS AMT
      FROM (SELECT ACCOUNT,
                   NVL(
                           CASE
                               WHEN SUBSTR(ACCOUNT, 1, 1) = '3' THEN SUM(ENDBAL_DEBIT) + SUM(ENDBAL_CREDIT)
                               WHEN SUBSTR(ACCOUNT, 1, 1) = '4' THEN SUM(ENDBAL_CREDIT) - SUM(ENDBAL_DEBIT)
                               WHEN SUBSTR(ACCOUNT, 1, 1) = '5' THEN (SUM(ENDBAL_DEBIT) - SUM(ENDBAL_CREDIT)) * -1
                               WHEN SUBSTR(ACCOUNT, 1, 1) = '6' THEN SUM(ENDBAL_CREDIT) - SUM(ENDBAL_DEBIT)
                               WHEN SUBSTR(ACCOUNT, 1, 1) = '7' THEN (SUM(ENDBAL_DEBIT) + SUM(ENDBAL_CREDIT)) * -1
                               ELSE SUM(ENDBAL_DEBIT) + SUM(ENDBAL_CREDIT)
                               END
                       , 0) AS AMT
            FROM (SELECT RB.ACCOUNT,
                         RB.CURRENCY,
                         RB.SUBACCOUNT_TYPE,
                         RB.TRANS_FLAG,
                         RB.OPENBAL_DEBIT,
                         RB.OPENBAL_CREDIT,
                         RB.DEBIT,
                         RB.CREDIT,
                         CASE
                             WHEN RB.SUBACCOUNT_TYPE = 0 AND RB.RAW_ENDBAL_DEBIT < 0 THEN 0
                             WHEN RB.SUBACCOUNT_TYPE = 0 THEN RB.RAW_ENDBAL_DEBIT
                             WHEN RB.SUBACCOUNT_TYPE = 1 AND RB.RAW_ENDBAL_CREDIT < 0 THEN ABS(RB.RAW_ENDBAL_CREDIT)
                             WHEN RB.SUBACCOUNT_TYPE = 9 THEN RB.RAW_ENDBAL_DEBIT
                             ELSE 0
                             END AS ENDBAL_DEBIT,
                         CASE
                             WHEN RB.SUBACCOUNT_TYPE = 0 AND RB.RAW_ENDBAL_DEBIT < 0 THEN ABS(RB.RAW_ENDBAL_DEBIT)
                             WHEN RB.SUBACCOUNT_TYPE = 1 AND RB.RAW_ENDBAL_CREDIT < 0 THEN 0
                             WHEN RB.SUBACCOUNT_TYPE = 1 THEN RB.RAW_ENDBAL_CREDIT
                             WHEN RB.SUBACCOUNT_TYPE = 9 THEN RB.RAW_ENDBAL_CREDIT
                             ELSE 0
                             END AS ENDBAL_CREDIT
                  FROM RAW_BALANCES RB)
            GROUP BY ACCOUNT)
      GROUP BY ACCOUNT) A, MKBD.MKBD_COA_MAPPER B
      WHERE B.ACCOUNT_TYPE =  '1'
      AND B.IS_ENABLED = 'Y'
      AND A.ACCOUNT = B.ACCOUNT
ORDER BY B.VD_SEKURITAS_CAT, B.VD_LINE_NO, A.ACCOUNT, A.AMT
     ) A LEFT JOIN DENPASAR.ACCOUNT B
ON A.ACCOUNT = B.CODE