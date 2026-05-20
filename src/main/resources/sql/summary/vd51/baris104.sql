WITH DEBITS_10401021 AS (SELECT MAX(ID)                     AS ID,
                                TRANS_MODULE,
                                MAX(TRANS_NO)               AS TRANS_NO,
                                TRANS_DATE,
                                TRANS_DUEDATE,
                                ACCOUNT,
                                SUBACCOUNT_TYPE,
                                SUBACCOUNT,
                                BRANCH,
                                NFAKTUR,
                                CURRENCY,
                                EXCHANGERATE,
                                SUM(AMOUNT)                 AS DEBIT,
                                SUM(AMOUNTIDR)              AS DEBITIDR,
                                CAST(NULL AS VARCHAR2(255)) AS DESCRIPTION
                         FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                         WHERE ACCOUNT = '10401021'
                           AND SUBACCOUNT_TYPE = 1
                           AND TRANS_MODULE IN (20, 21, 22)
                           AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                           AND TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                         GROUP BY TRANS_MODULE, TRANS_DATE, TRANS_DUEDATE, ACCOUNT, SUBACCOUNT_TYPE,
                                  SUBACCOUNT, BRANCH, NFAKTUR, CURRENCY, EXCHANGERATE
                         HAVING SUM(AMOUNT) > 0
                         UNION ALL
                         SELECT ID,
                                TRANS_MODULE,
                                TRANS_NO,
                                TRANS_DATE,
                                TRANS_DUEDATE,
                                ACCOUNT,
                                SUBACCOUNT_TYPE,
                                SUBACCOUNT,
                                BRANCH,
                                NFAKTUR,
                                CURRENCY,
                                EXCHANGERATE,
                                AMOUNT    AS DEBIT,
                                AMOUNTIDR AS DEBITIDR,
                                DESCRIPTION
                         FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                         WHERE ACCOUNT = '10401021'
                           AND SUBACCOUNT_TYPE = 1
                           AND TRANS_MODULE NOT IN (20, 21, 22)
                           AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                           AND TRANS_DATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND AMOUNT > 0),

     CREDITS_10401021 AS (SELECT ID,
                                 TRANS_MODULE,
                                 TRANS_NO,
                                 TRANS_DATE,
                                 TRANS_DUEDATE,
                                 ACCOUNT,
                                 SUBACCOUNT_TYPE,
                                 SUBACCOUNT,
                                 BRANCH,
                                 NFAKTUR,
                                 CURRENCY,
                                 EXCHANGERATE,
                                 -AMOUNT    AS CREDIT,
                                 -AMOUNTIDR AS CREDITIDR,
                                 DESCRIPTION
                          FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                          WHERE ACCOUNT = '10401021'
                            AND SUBACCOUNT_TYPE = 1
                            AND TRANS_MODULE NOT IN (20, 21, 22)
                            AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                            AND TRANS_DATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND AMOUNT < 0
                          UNION ALL
                          SELECT MAX(ID)                     AS ID,
                                 TRANS_MODULE,
                                 MAX(TRANS_NO)               AS TRANS_NO,
                                 TRANS_DATE,
                                 TRANS_DUEDATE,
                                 ACCOUNT,
                                 SUBACCOUNT_TYPE,
                                 SUBACCOUNT,
                                 BRANCH,
                                 NFAKTUR,
                                 CURRENCY,
                                 EXCHANGERATE,
                                 -SUM(AMOUNT)                AS CREDIT,
                                 -SUM(AMOUNTIDR)             AS CREDITIDR,
                                 CAST(NULL AS VARCHAR2(255)) AS DESCRIPTION
                          FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                          WHERE ACCOUNT = '10401021'
                            AND SUBACCOUNT_TYPE = 1
                            AND TRANS_MODULE IN (20, 21, 22)
                            AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                            AND TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                          GROUP BY TRANS_MODULE, TRANS_DATE, TRANS_DUEDATE, ACCOUNT, SUBACCOUNT_TYPE,
                                   SUBACCOUNT, BRANCH, NFAKTUR, CURRENCY, EXCHANGERATE
                          HAVING SUM(AMOUNT) < 0),

     CREDIT_TOTALS_10401021 AS (SELECT ACCOUNT,
                                       SUBACCOUNT_TYPE,
                                       SUBACCOUNT,
                                       BRANCH,
                                       CURRENCY,
                                       SUM(CREDIT)    AS TOTAL_CREDIT,
                                       SUM(CREDITIDR) AS TOTAL_CREDITIDR
                                FROM CREDITS_10401021
                                GROUP BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY),

     DEBIT_TOTALS_10401021 AS (SELECT ACCOUNT,
                                      SUBACCOUNT_TYPE,
                                      SUBACCOUNT,
                                      BRANCH,
                                      CURRENCY,
                                      SUM(DEBIT)    AS TOTAL_DEBIT,
                                      SUM(DEBITIDR) AS TOTAL_DEBITIDR
                               FROM DEBITS_10401021
                               GROUP BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY),

     DEBITS_NUMBERED_10401021 AS (SELECT D.*,
                                         NVL(SUM(DEBIT) OVER (
                                             PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                             ORDER BY TRANS_DUEDATE, TRANS_DATE, ID
                                             ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING
                                             ), 0) AS CUM_DEBIT_BEFORE,
                                         SUM(DEBIT) OVER (
                                             PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                             ORDER BY TRANS_DUEDATE, TRANS_DATE, ID
                                             ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                                             )     AS CUM_DEBIT_AFTER
                                  FROM DEBITS_10401021 D),

     MATCHED_DEBITS_10401021 AS (SELECT D.ID,
                                        D.TRANS_MODULE,
                                        D.TRANS_NO,
                                        D.TRANS_DATE,
                                        D.TRANS_DUEDATE,
                                        D.ACCOUNT,
                                        D.SUBACCOUNT_TYPE,
                                        D.SUBACCOUNT,
                                        D.BRANCH,
                                        D.NFAKTUR,
                                        D.CURRENCY,
                                        D.EXCHANGERATE,
                                        D.DESCRIPTION,
                                        D.DEBIT    AS ORIGINAL_DEBIT,
                                        D.DEBITIDR AS ORIGINAL_DEBITIDR,
                                        CASE
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) <= D.CUM_DEBIT_BEFORE THEN D.DEBIT
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) >= D.CUM_DEBIT_AFTER THEN 0
                                            ELSE D.CUM_DEBIT_AFTER - CT.TOTAL_CREDIT
                                            END    AS REMAINING_DEBIT,
                                        CASE
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) <= D.CUM_DEBIT_BEFORE THEN D.DEBITIDR
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) >= D.CUM_DEBIT_AFTER THEN 0
                                            ELSE CASE
                                                     WHEN D.DEBIT = 0 THEN 0
                                                     ELSE D.DEBITIDR * ((D.CUM_DEBIT_AFTER - CT.TOTAL_CREDIT) / D.DEBIT) END
                                            END    AS REMAINING_DEBITIDR
                                 FROM DEBITS_NUMBERED_10401021 D
                                          LEFT JOIN CREDIT_TOTALS_10401021 CT
                                                    ON D.ACCOUNT = CT.ACCOUNT AND D.SUBACCOUNT_TYPE = CT.SUBACCOUNT_TYPE
                                                        AND D.SUBACCOUNT = CT.SUBACCOUNT AND D.BRANCH = CT.BRANCH AND
                                                       D.CURRENCY = CT.CURRENCY),

     CREDITS_NUMBERED_10401021 AS (SELECT C.*,
                                          NVL(SUM(CREDIT) OVER (
                                              PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                              ORDER BY NFAKTUR, TRANS_DUEDATE, TRANS_DATE, ID
                                              ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING
                                              ), 0) AS CUM_CREDIT_BEFORE,
                                          SUM(CREDIT) OVER (
                                              PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                              ORDER BY NFAKTUR, TRANS_DUEDATE, TRANS_DATE, ID
                                              ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                                              )     AS CUM_CREDIT_AFTER
                                   FROM CREDITS_10401021 C),

     UNMATCHED_CREDITS_10401021 AS (SELECT C.ID,
                                           C.TRANS_MODULE,
                                           C.TRANS_NO,
                                           C.TRANS_DATE,
                                           C.TRANS_DUEDATE,
                                           C.ACCOUNT,
                                           C.SUBACCOUNT_TYPE,
                                           C.SUBACCOUNT,
                                           C.BRANCH,
                                           C.NFAKTUR,
                                           C.CURRENCY,
                                           C.EXCHANGERATE,
                                           C.DESCRIPTION,
                                           CASE
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) >= C.CUM_CREDIT_AFTER THEN 0
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) <= C.CUM_CREDIT_BEFORE THEN -C.CREDIT
                                               ELSE -(C.CUM_CREDIT_AFTER - DT.TOTAL_DEBIT)
                                               END AS UNMATCHED_DEBIT,
                                           CASE
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) >= C.CUM_CREDIT_AFTER THEN 0
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) <= C.CUM_CREDIT_BEFORE THEN -C.CREDITIDR
                                               ELSE CASE
                                                        WHEN C.CREDIT = 0 THEN 0
                                                        ELSE -C.CREDITIDR * ((C.CUM_CREDIT_AFTER - DT.TOTAL_DEBIT) / C.CREDIT) END
                                               END AS UNMATCHED_DEBITIDR
                                    FROM CREDITS_NUMBERED_10401021 C
                                             LEFT JOIN DEBIT_TOTALS_10401021 DT
                                                       ON C.ACCOUNT = DT.ACCOUNT AND
                                                          C.SUBACCOUNT_TYPE = DT.SUBACCOUNT_TYPE
                                                           AND C.SUBACCOUNT = DT.SUBACCOUNT AND C.BRANCH = DT.BRANCH AND
                                                          C.CURRENCY = DT.CURRENCY),

     AGING_10401021 AS (SELECT ID,
                               TRANS_MODULE,
                               TRANS_NO,
                               TRANS_DATE,
                               TRANS_DUEDATE,
                               ACCOUNT,
                               SUBACCOUNT_TYPE,
                               SUBACCOUNT,
                               BRANCH,
                               NFAKTUR,
                               CURRENCY,
                               EXCHANGERATE,
                               REMAINING_DEBIT                                 AS DEBIT,
                               REMAINING_DEBITIDR                              AS DEBITIDR,
                               DESCRIPTION,
                               CASE WHEN REMAINING_DEBIT < 0 THEN 1 ELSE 0 END AS PL
                        FROM MATCHED_DEBITS_10401021
                        WHERE REMAINING_DEBIT <> 0
                        UNION ALL
                        SELECT ID,
                               TRANS_MODULE,
                               TRANS_NO,
                               TRANS_DATE,
                               TRANS_DUEDATE,
                               ACCOUNT,
                               SUBACCOUNT_TYPE,
                               SUBACCOUNT,
                               BRANCH,
                               NFAKTUR,
                               CURRENCY,
                               EXCHANGERATE,
                               UNMATCHED_DEBIT    AS DEBIT,
                               UNMATCHED_DEBITIDR AS DEBITIDR,
                               DESCRIPTION,
                               1                  AS PL
                        FROM UNMATCHED_CREDITS_10401021
                        WHERE UNMATCHED_DEBIT <> 0),

-- ============================================================================
-- ACCOUNT 10401031 - Second account processing
-- ============================================================================
     DEBITS_10401031 AS (SELECT MAX(ID)                     AS ID,
                                TRANS_MODULE,
                                MAX(TRANS_NO)               AS TRANS_NO,
                                TRANS_DATE,
                                TRANS_DUEDATE,
                                ACCOUNT,
                                SUBACCOUNT_TYPE,
                                SUBACCOUNT,
                                BRANCH,
                                NFAKTUR,
                                CURRENCY,
                                EXCHANGERATE,
                                SUM(AMOUNT)                 AS DEBIT,
                                SUM(AMOUNTIDR)              AS DEBITIDR,
                                CAST(NULL AS VARCHAR2(255)) AS DESCRIPTION
                         FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                         WHERE ACCOUNT = '10401031'
                           AND SUBACCOUNT_TYPE = 1
                           AND TRANS_MODULE IN (20, 21, 22)
                           AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                           AND TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                         GROUP BY TRANS_MODULE, TRANS_DATE, TRANS_DUEDATE, ACCOUNT, SUBACCOUNT_TYPE,
                                  SUBACCOUNT, BRANCH, NFAKTUR, CURRENCY, EXCHANGERATE
                         HAVING SUM(AMOUNT) > 0
                         UNION ALL
                         SELECT ID,
                                TRANS_MODULE,
                                TRANS_NO,
                                TRANS_DATE,
                                TRANS_DUEDATE,
                                ACCOUNT,
                                SUBACCOUNT_TYPE,
                                SUBACCOUNT,
                                BRANCH,
                                NFAKTUR,
                                CURRENCY,
                                EXCHANGERATE,
                                AMOUNT    AS DEBIT,
                                AMOUNTIDR AS DEBITIDR,
                                DESCRIPTION
                         FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                         WHERE ACCOUNT = '10401031'
                           AND SUBACCOUNT_TYPE = 1
                           AND TRANS_MODULE NOT IN (20, 21, 22)
                           AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                           AND TRANS_DATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                           AND AMOUNT > 0),

     CREDITS_10401031 AS (SELECT ID,
                                 TRANS_MODULE,
                                 TRANS_NO,
                                 TRANS_DATE,
                                 TRANS_DUEDATE,
                                 ACCOUNT,
                                 SUBACCOUNT_TYPE,
                                 SUBACCOUNT,
                                 BRANCH,
                                 NFAKTUR,
                                 CURRENCY,
                                 EXCHANGERATE,
                                 -AMOUNT    AS CREDIT,
                                 -AMOUNTIDR AS CREDITIDR,
                                 DESCRIPTION
                          FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                          WHERE ACCOUNT = '10401031'
                            AND SUBACCOUNT_TYPE = 1
                            AND TRANS_MODULE NOT IN (20, 21, 22)
                            AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                            AND TRANS_DATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND AMOUNT < 0
                          UNION ALL
                          SELECT MAX(ID)                     AS ID,
                                 TRANS_MODULE,
                                 MAX(TRANS_NO)               AS TRANS_NO,
                                 TRANS_DATE,
                                 TRANS_DUEDATE,
                                 ACCOUNT,
                                 SUBACCOUNT_TYPE,
                                 SUBACCOUNT,
                                 BRANCH,
                                 NFAKTUR,
                                 CURRENCY,
                                 EXCHANGERATE,
                                 -SUM(AMOUNT)                AS CREDIT,
                                 -SUM(AMOUNTIDR)             AS CREDITIDR,
                                 CAST(NULL AS VARCHAR2(255)) AS DESCRIPTION
                          FROM DENPASAR.LEDGER_HISTORICAL@CAMMON
                          WHERE ACCOUNT = '10401031'
                            AND SUBACCOUNT_TYPE = 1
                            AND TRANS_MODULE IN (20, 21, 22)
                            AND TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'YYYYMM')
                            AND TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                            AND GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                          GROUP BY TRANS_MODULE, TRANS_DATE, TRANS_DUEDATE, ACCOUNT, SUBACCOUNT_TYPE,
                                   SUBACCOUNT, BRANCH, NFAKTUR, CURRENCY, EXCHANGERATE
                          HAVING SUM(AMOUNT) < 0),

     CREDIT_TOTALS_10401031 AS (SELECT ACCOUNT,
                                       SUBACCOUNT_TYPE,
                                       SUBACCOUNT,
                                       BRANCH,
                                       CURRENCY,
                                       SUM(CREDIT)    AS TOTAL_CREDIT,
                                       SUM(CREDITIDR) AS TOTAL_CREDITIDR
                                FROM CREDITS_10401031
                                GROUP BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY),

     DEBIT_TOTALS_10401031 AS (SELECT ACCOUNT,
                                      SUBACCOUNT_TYPE,
                                      SUBACCOUNT,
                                      BRANCH,
                                      CURRENCY,
                                      SUM(DEBIT)    AS TOTAL_DEBIT,
                                      SUM(DEBITIDR) AS TOTAL_DEBITIDR
                               FROM DEBITS_10401031
                               GROUP BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY),

     DEBITS_NUMBERED_10401031 AS (SELECT D.*,
                                         NVL(SUM(DEBIT) OVER (
                                             PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                             ORDER BY TRANS_DUEDATE, TRANS_DATE, ID
                                             ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING
                                             ), 0) AS CUM_DEBIT_BEFORE,
                                         SUM(DEBIT) OVER (
                                             PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                             ORDER BY TRANS_DUEDATE, TRANS_DATE, ID
                                             ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                                             )     AS CUM_DEBIT_AFTER
                                  FROM DEBITS_10401031 D),

     MATCHED_DEBITS_10401031 AS (SELECT D.ID,
                                        D.TRANS_MODULE,
                                        D.TRANS_NO,
                                        D.TRANS_DATE,
                                        D.TRANS_DUEDATE,
                                        D.ACCOUNT,
                                        D.SUBACCOUNT_TYPE,
                                        D.SUBACCOUNT,
                                        D.BRANCH,
                                        D.NFAKTUR,
                                        D.CURRENCY,
                                        D.EXCHANGERATE,
                                        D.DESCRIPTION,
                                        D.DEBIT    AS ORIGINAL_DEBIT,
                                        D.DEBITIDR AS ORIGINAL_DEBITIDR,
                                        CASE
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) <= D.CUM_DEBIT_BEFORE THEN D.DEBIT
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) >= D.CUM_DEBIT_AFTER THEN 0
                                            ELSE D.CUM_DEBIT_AFTER - CT.TOTAL_CREDIT
                                            END    AS REMAINING_DEBIT,
                                        CASE
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) <= D.CUM_DEBIT_BEFORE THEN D.DEBITIDR
                                            WHEN NVL(CT.TOTAL_CREDIT, 0) >= D.CUM_DEBIT_AFTER THEN 0
                                            ELSE CASE
                                                     WHEN D.DEBIT = 0 THEN 0
                                                     ELSE D.DEBITIDR * ((D.CUM_DEBIT_AFTER - CT.TOTAL_CREDIT) / D.DEBIT) END
                                            END    AS REMAINING_DEBITIDR
                                 FROM DEBITS_NUMBERED_10401031 D
                                          LEFT JOIN CREDIT_TOTALS_10401031 CT
                                                    ON D.ACCOUNT = CT.ACCOUNT AND D.SUBACCOUNT_TYPE = CT.SUBACCOUNT_TYPE
                                                        AND D.SUBACCOUNT = CT.SUBACCOUNT AND D.BRANCH = CT.BRANCH AND
                                                       D.CURRENCY = CT.CURRENCY),

     CREDITS_NUMBERED_10401031 AS (SELECT C.*,
                                          NVL(SUM(CREDIT) OVER (
                                              PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                              ORDER BY NFAKTUR, TRANS_DUEDATE, TRANS_DATE, ID
                                              ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING
                                              ), 0) AS CUM_CREDIT_BEFORE,
                                          SUM(CREDIT) OVER (
                                              PARTITION BY ACCOUNT, SUBACCOUNT_TYPE, SUBACCOUNT, BRANCH, CURRENCY
                                              ORDER BY NFAKTUR, TRANS_DUEDATE, TRANS_DATE, ID
                                              ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                                              )     AS CUM_CREDIT_AFTER
                                   FROM CREDITS_10401031 C),

     UNMATCHED_CREDITS_10401031 AS (SELECT C.ID,
                                           C.TRANS_MODULE,
                                           C.TRANS_NO,
                                           C.TRANS_DATE,
                                           C.TRANS_DUEDATE,
                                           C.ACCOUNT,
                                           C.SUBACCOUNT_TYPE,
                                           C.SUBACCOUNT,
                                           C.BRANCH,
                                           C.NFAKTUR,
                                           C.CURRENCY,
                                           C.EXCHANGERATE,
                                           C.DESCRIPTION,
                                           CASE
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) >= C.CUM_CREDIT_AFTER THEN 0
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) <= C.CUM_CREDIT_BEFORE THEN -C.CREDIT
                                               ELSE -(C.CUM_CREDIT_AFTER - DT.TOTAL_DEBIT)
                                               END AS UNMATCHED_DEBIT,
                                           CASE
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) >= C.CUM_CREDIT_AFTER THEN 0
                                               WHEN NVL(DT.TOTAL_DEBIT, 0) <= C.CUM_CREDIT_BEFORE THEN -C.CREDITIDR
                                               ELSE CASE
                                                        WHEN C.CREDIT = 0 THEN 0
                                                        ELSE -C.CREDITIDR * ((C.CUM_CREDIT_AFTER - DT.TOTAL_DEBIT) / C.CREDIT) END
                                               END AS UNMATCHED_DEBITIDR
                                    FROM CREDITS_NUMBERED_10401031 C
                                             LEFT JOIN DEBIT_TOTALS_10401031 DT
                                                       ON C.ACCOUNT = DT.ACCOUNT AND
                                                          C.SUBACCOUNT_TYPE = DT.SUBACCOUNT_TYPE
                                                           AND C.SUBACCOUNT = DT.SUBACCOUNT AND C.BRANCH = DT.BRANCH AND
                                                          C.CURRENCY = DT.CURRENCY),

     AGING_10401031 AS (SELECT ID,
                               TRANS_MODULE,
                               TRANS_NO,
                               TRANS_DATE,
                               TRANS_DUEDATE,
                               ACCOUNT,
                               SUBACCOUNT_TYPE,
                               SUBACCOUNT,
                               BRANCH,
                               NFAKTUR,
                               CURRENCY,
                               EXCHANGERATE,
                               REMAINING_DEBIT                                 AS DEBIT,
                               REMAINING_DEBITIDR                              AS DEBITIDR,
                               DESCRIPTION,
                               CASE WHEN REMAINING_DEBIT < 0 THEN 1 ELSE 0 END AS PL
                        FROM MATCHED_DEBITS_10401031
                        WHERE REMAINING_DEBIT <> 0
                        UNION ALL
                        SELECT ID,
                               TRANS_MODULE,
                               TRANS_NO,
                               TRANS_DATE,
                               TRANS_DUEDATE,
                               ACCOUNT,
                               SUBACCOUNT_TYPE,
                               SUBACCOUNT,
                               BRANCH,
                               NFAKTUR,
                               CURRENCY,
                               EXCHANGERATE,
                               UNMATCHED_DEBIT    AS DEBIT,
                               UNMATCHED_DEBITIDR AS DEBITIDR,
                               DESCRIPTION,
                               1                  AS PL
                        FROM UNMATCHED_CREDITS_10401031
                        WHERE UNMATCHED_DEBIT <> 0),

     AGING_RECEIVABLE AS (SELECT *
                          FROM AGING_10401021
                          UNION ALL
                          SELECT *
                          FROM AGING_10401031)
SELECT OUTSTANDING_AMOUNT,
       INTEREST_AMOUNT,
       CUSTODY_AMOUNT,
       GL_SUM,
       '('||INTEREST_AMOUNT||' + ' ||CUSTODY_AMOUNT||' + ('|| GL_SUM ||' - (' || OUTSTANDING_AMOUNT || ' + ' || INTEREST_AMOUNT ||' + ' || CUSTODY_AMOUNT || ')))' AS FORMULA,
       (INTEREST_AMOUNT + CUSTODY_AMOUNT + (GL_SUM - (OUTSTANDING_AMOUNT + INTEREST_AMOUNT + CUSTODY_AMOUNT))) AS TOTAL
FROM (SELECT SUM(HUTANG_TRANSAKSI)                   AS OUTSTANDING_AMOUNT,
             SUM(DENDA_TELAT_BAYAR)                  AS INTEREST_AMOUNT,
             SUM(BIAYA_CUSTODY)                      AS CUSTODY_AMOUNT,
             MAX((SELECT NVL(SUM(TOTAL_AMT), 0) AS AMT
                  FROM (SELECT ACCOUNT, SUBACCOUNT, SUM(AMOUNTIDR) AS TOTAL_AMT
                        FROM (SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR) AS AMOUNTIDR
                              FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                       INNER JOIN DENPASAR.ACCOUNT A ON L.ACCOUNT = A.CODE
                              WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'yyyymm')
                                AND L.TRANS_CURRENT = 0
                                AND A.SUBACCOUNT_TYPE > 0
                                AND L.TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                                AND L.ACCOUNT IN ('10401021', '10401022', '10401031', '10401071')
                                AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                              GROUP BY L.ACCOUNT, L.SUBACCOUNT

                              UNION ALL

                              SELECT L.ACCOUNT, L.SUBACCOUNT, SUM(L.AMOUNTIDR)
                              FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                       INNER JOIN DENPASAR.ACCOUNT A ON L.ACCOUNT = A.CODE
                              WHERE L.ACCOUNT IN ('10401021', '10401022', '10401031', '10401071')
                                AND L.TRANS_DATE >= TRUNC(TO_DATE(:genDate, 'yyyy-mm-dd'), 'MM')
                                AND L.TRANS_DUEDATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
                                AND L.TRANS_CURRENT = 1
                                AND A.SUBACCOUNT_TYPE > 0
                              AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                              GROUP BY L.ACCOUNT, L.SUBACCOUNT)
                        GROUP BY ACCOUNT, SUBACCOUNT
                        HAVING SUM(AMOUNTIDR) > 0))) AS GL_SUM
      FROM (SELECT C.CODE,
                   C.NAME,
                   C.SID,
                   COALESCE(HUTANG.OUTSTANDING, 0)  AS HUTANG_TRANSAKSI,
                   COALESCE(DENDA.OUTSTANDING, 0)   AS DENDA_TELAT_BAYAR,
                   COALESCE(CUSTODY.OUTSTANDING, 0) AS BIAYA_CUSTODY
            FROM DENPASAR.CLIENT C
                     -- HUTANG: NOT IN (12,13,15) OR (IN (13,15) AND DEVIDEN/W/O/IPO)
                     LEFT JOIN (SELECT SUBACCOUNT AS CODE, SUM(DEBITIDR) AS OUTSTANDING
                                FROM AGING_RECEIVABLE
                                WHERE DEBIT <> 0
                                  AND (
                                    TRANS_MODULE NOT IN (12, 13, 15)
                                        OR (TRANS_MODULE IN (13, 15) AND DESCRIPTION LIKE '%DEVIDEN%')
                                        OR (TRANS_MODULE IN (13, 15) AND DESCRIPTION LIKE '%W/O%')
                                        OR (TRANS_MODULE IN (13, 15) AND DESCRIPTION LIKE '%IPO')
                                    )
                                GROUP BY SUBACCOUNT
                                HAVING SUM(DEBITIDR) > 0) HUTANG ON C.CODE = HUTANG.CODE
                -- DENDA: Module 12
                     LEFT JOIN (SELECT SUBACCOUNT AS CODE, SUM(DEBITIDR) AS OUTSTANDING
                                FROM AGING_RECEIVABLE
                                WHERE DEBIT <> 0
                                  AND TRANS_MODULE = 12
                                GROUP BY SUBACCOUNT
                                HAVING SUM(DEBITIDR) > 0) DENDA ON C.CODE = DENDA.CODE
                -- CUSTODY: IN (13,15) AND CUSTODY/KUSTODI/METERAI/MARKET DATA
                     LEFT JOIN (SELECT SUBACCOUNT AS CODE, SUM(DEBITIDR) AS OUTSTANDING
                                FROM AGING_RECEIVABLE
                                WHERE DEBIT <> 0
                                  AND TRANS_MODULE IN (13, 15)
                                  AND (
                                    DESCRIPTION LIKE '%CUSTODY%'
                                        OR DESCRIPTION LIKE '%KUSTODI%'
                                        OR DESCRIPTION LIKE '%METERAI%'
                                        OR DESCRIPTION LIKE '%MARKET DATA%'
                                    )
                                GROUP BY SUBACCOUNT
                                HAVING SUM(DEBITIDR) > 0) CUSTODY ON C.CODE = CUSTODY.CODE
            WHERE (HUTANG.OUTSTANDING > 0 OR DENDA.OUTSTANDING > 0 OR CUSTODY.OUTSTANDING > 0)))