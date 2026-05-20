SELECT 'Regular Netting Transactions (Non NG), Exclude Institution and House' AS BOARD, SUM(AMOUNT) AS TOTAL_AMOUNT
                           FROM (SELECT CL_CODE,
                                        DEAL_DATE,
                                        CASE
                                            WHEN BUY_AMOUNT > SELL_AMOUNT THEN BUY_AMOUNT - SELL_AMOUNT END AS AMOUNT
                                 FROM (SELECT CL_CODE,
                                              DEAL_DATE,
                                              NVL(SUM(DECODE(DEAL_CODE, '0', CL_TOTAL, 0)), 0) AS BUY_AMOUNT,
                                              NVL(SUM(DECODE(DEAL_CODE, '1', CL_TOTAL, 0)), 0) AS SELL_AMOUNT
                                       FROM DENPASAR.REGULAR A
                                       WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyy-mm-dd'), -2) AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyy-mm-dd'), 0)
                                         AND A.BOARD != 'NG'
                                         AND A.CL_INVOICE_DUE > TO_DATE('2026-04-21', 'yyyy-mm-dd')
                                         AND A.CL_CODE NOT LIKE '17%'
                                         AND A.CL_CODE NOT LIKE '11%'
                                         AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')
                                       GROUP BY CL_CODE, DEAL_DATE)
                                 WHERE BUY_AMOUNT > SELL_AMOUNT)
            
                           UNION ALL
            
                           SELECT 'Buy Transactions (NG) - Exclude Institution and House' AS BOARD, SUM(CL_TOTAL) AS TOTAL_AMOUNT
                           FROM DENPASAR.REGULAR A
                           WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyy-mm-dd'), -2) AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyy-mm-dd'), 0)
                             AND A.BOARD = 'NG'
                             AND A.CL_INVOICE_DUE > TO_DATE('2026-04-21', 'yyyy-mm-dd')
                             AND A.CL_CODE NOT LIKE '17%'
                             AND A.CL_CODE NOT LIKE '11%'
                             AND A.DEAL_CODE = 0
                           GROUP BY CL_CODE, DEAL_DATE