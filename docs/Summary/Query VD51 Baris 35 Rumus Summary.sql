SELECT B.NAME, A.*
                     FROM (SELECT SUBACCOUNT, SUM(AMOUNTIDR) AS AMOUNTIDR
                           FROM (SELECT L.SUBACCOUNT,
                                        SUM(L.AMOUNTIDR) AS AMOUNTIDR
                                 FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                                          INNER JOIN DENPASAR.ACCOUNT A ON L.ACCOUNT = A.CODE
                                 WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm') AND L.TRANS_DUEDATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                   AND L.TRANS_CURRENT = 0
                                   AND A.SUBACCOUNT_TYPE > 0
                                   AND L.ACCOUNT = '10401131'
                                   AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                 GROUP BY L.SUBACCOUNT

                                 UNION ALL


                                 SELECT SUBACCOUNT, SUM(AMOUNTIDR)
                                 FROM DENPASAR.LEDGER_HISTORICAL@cammon
                                 WHERE ACCOUNT = '10401131'
                                   AND (TRANS_DATE >= TRUNC(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'MM') AND TRANS_DUEDATE <= TO_DATE('2026-04-21', 'yyyy-mm-dd'))
                                   AND TRANS_CURRENT = 1
                                   AND GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                 GROUP BY SUBACCOUNT)
                           HAVING SUM(AMOUNTIDR) > 0
                           GROUP BY SUBACCOUNT) A, DENPASAR.CLIENT B
WHERE B.CODE = A.SUBACCOUNT