SELECT 'LEDGER (Non Interest Agreement)' AS SOURCE,
       NVL(SUM(AMOUNTIDR), 0) AS AMT
FROM (
    SELECT SUBACCOUNT, ABS(SUM(AMOUNTIDR)) AS AMOUNTIDR
    FROM (
        SELECT ACCOUNT, SUBACCOUNT, SUM(AMOUNTIDR) AS AMOUNTIDR
        FROM (
            SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR) AS AMOUNTIDR
            FROM DENPASAR.LEDGER_HISTORICAL@cammon L
            INNER JOIN DENPASAR.ACCOUNT A ON L.ACCOUNT = A.CODE
            WHERE L.TRANS_PERIOD    = TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyymm')
              AND L.TRANS_CURRENT   = 0
              AND A.SUBACCOUNT_TYPE > 0
              AND L.ACCOUNT IN ('10401021','10401022','10401031','10401071','10401131')
              AND L.TRANS_DUEDATE  <= TO_DATE(:genDate,'yyyy-mm-dd')
              AND L.GEN_DATE        = TO_DATE(:genDate,'yyyy-mm-dd')
            GROUP BY L.ACCOUNT, L.SUBACCOUNT
            UNION ALL
            SELECT ACCOUNT, SUBACCOUNT, SUM(AMOUNTIDR)
            FROM DENPASAR.LEDGER_HISTORICAL@cammon
            WHERE ACCOUNT IN ('10401021','10401022','10401031','10401071','10401131')
              AND TRANS_DATE    >= TRUNC(TO_DATE(:genDate,'yyyy-mm-dd'),'MM')
              AND TRANS_DUEDATE <= TO_DATE(:genDate,'yyyy-mm-dd')
              AND TRANS_CURRENT  > 0
              AND GEN_DATE       = TO_DATE(:genDate,'yyyy-mm-dd')
            GROUP BY ACCOUNT, SUBACCOUNT
        )
        GROUP BY ACCOUNT, SUBACCOUNT
        HAVING SUM(AMOUNTIDR) < 0
    )
    GROUP BY SUBACCOUNT
) A
LEFT JOIN DENPASAR.CLIENT_PROPERTY B ON A.SUBACCOUNT = B.CLIENT_ID AND B.INTEREST_AGRREMENT = 1
WHERE B.CLIENT_ID IS NULL

UNION ALL

SELECT 'MKBD_BALANCE (Account 20689991)' AS SOURCE,
       NVL(
           NVL(SUM(CASE WHEN ENDBAL_DEBIT  < 0 THEN -ENDBAL_DEBIT  END), 0)
         + NVL(SUM(CASE WHEN ENDBAL_CREDIT > 0 THEN  ENDBAL_CREDIT END), 0)
       , 0) AS AMT
FROM MKBD.MKBD_BALANCE_SUMMARY
WHERE GEN_DATE    = TO_DATE(:genDate,'yyyy-mm-dd')
  AND ACCOUNT     = '20689991'
  AND LENGTH(ACCOUNT) = 8
  AND MKBD_FLAG   = 0

UNION ALL

SELECT 'TOTAL AMT' AS SOURCE,
       (
           SELECT NVL(SUM(AMOUNTIDR), 0)
           FROM (
               SELECT SUBACCOUNT, ABS(SUM(AMOUNTIDR)) AS AMOUNTIDR
               FROM (
                   SELECT ACCOUNT, SUBACCOUNT, SUM(AMOUNTIDR) AS AMOUNTIDR
                   FROM (
                       SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR) AS AMOUNTIDR
                       FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                       INNER JOIN DENPASAR.ACCOUNT A ON L.ACCOUNT = A.CODE
                       WHERE L.TRANS_PERIOD    = TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyymm')
                         AND L.TRANS_CURRENT   = 0
                         AND A.SUBACCOUNT_TYPE > 0
                         AND L.ACCOUNT IN ('10401021','10401022','10401031','10401071','10401131')
                         AND L.TRANS_DUEDATE  <= TO_DATE(:genDate,'yyyy-mm-dd')
                         AND L.GEN_DATE        = TO_DATE(:genDate,'yyyy-mm-dd')
                       GROUP BY L.ACCOUNT, L.SUBACCOUNT
                       UNION ALL
                       SELECT ACCOUNT, SUBACCOUNT, SUM(AMOUNTIDR)
                       FROM DENPASAR.LEDGER_HISTORICAL@cammon
                       WHERE ACCOUNT IN ('10401021','10401022','10401031','10401071','10401131')
                         AND TRANS_DATE    >= TRUNC(TO_DATE(:genDate,'yyyy-mm-dd'),'MM')
                         AND TRANS_DUEDATE <= TO_DATE(:genDate,'yyyy-mm-dd')
                         AND TRANS_CURRENT  > 0
                         AND GEN_DATE       = TO_DATE(:genDate,'yyyy-mm-dd')
                       GROUP BY ACCOUNT, SUBACCOUNT
                   )
                   GROUP BY ACCOUNT, SUBACCOUNT
                   HAVING SUM(AMOUNTIDR) < 0
               )
               GROUP BY SUBACCOUNT
           ) A
           LEFT JOIN DENPASAR.CLIENT_PROPERTY B ON A.SUBACCOUNT = B.CLIENT_ID AND B.INTEREST_AGRREMENT = 1
           WHERE B.CLIENT_ID IS NULL
       )
       +
       (
           SELECT NVL(
                      NVL(SUM(CASE WHEN ENDBAL_DEBIT  < 0 THEN -ENDBAL_DEBIT  END), 0)
                    + NVL(SUM(CASE WHEN ENDBAL_CREDIT > 0 THEN  ENDBAL_CREDIT END), 0)
                  , 0)
           FROM MKBD.MKBD_BALANCE_SUMMARY
           WHERE GEN_DATE    = TO_DATE(:genDate,'yyyy-mm-dd')
             AND ACCOUNT     = '20689991'
             AND LENGTH(ACCOUNT) = 8
             AND MKBD_FLAG   = 0
       ) AS AMT
FROM DUAL;