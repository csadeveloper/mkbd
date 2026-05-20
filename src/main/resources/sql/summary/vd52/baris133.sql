SELECT BOARD, SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT
FROM (
    -- === Cabang 1: Non-NG Netting (SELL > BUY) ===
    SELECT 'Regular Netting Transactions (Non NG, Sell > Buy), Exclude Institution and House' AS BOARD,
           SUM(AMOUNT) AS TOTAL_AMOUNT
    FROM (
        SELECT CL_CODE,
               DEAL_DATE,
               CASE WHEN SELL_AMOUNT > BUY_AMOUNT THEN SELL_AMOUNT - BUY_AMOUNT END AS AMOUNT
        FROM (
            SELECT CL_CODE,
                   DEAL_DATE,
                   NVL(SUM(DECODE(DEAL_CODE, '0', CL_TOTAL, 0)), 0) AS BUY_AMOUNT,
                   NVL(SUM(DECODE(DEAL_CODE, '1', CL_TOTAL, 0)), 0) AS SELL_AMOUNT
            FROM DENPASAR.REGULAR A
            WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'), -2)
                                  AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'),  0)
              AND A.BOARD          != 'NG'
              AND A.CL_INVOICE_DUE  > TO_DATE(:genDate,'yyyy-mm-dd')
              AND A.CL_CODE NOT LIKE '17%'
              AND A.CL_CODE NOT LIKE '11%'
              AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')
            GROUP BY CL_CODE, DEAL_DATE
        )
        WHERE SELL_AMOUNT > BUY_AMOUNT
    )

    UNION ALL

    -- === Cabang 2: NG Sell (DEAL_CODE = '1') ===
    SELECT 'Sell Transactions (NG), Exclude Institution and House' AS BOARD,
           SUM(CL_TOTAL) AS TOTAL_AMOUNT
    FROM DENPASAR.REGULAR A
    WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'), -2)
                          AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'),  0)
      AND A.BOARD          = 'NG'
      AND A.CL_INVOICE_DUE > TO_DATE(:genDate,'yyyy-mm-dd')
      AND A.CL_CODE NOT LIKE '17%'
      AND A.CL_CODE NOT LIKE '11%'
      AND A.DEAL_CODE      = '1'
      AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')

    UNION ALL

    -- === TOTAL ===
    SELECT 'TOTAL AMT' AS BOARD,
           (
               SELECT NVL(SUM(AMOUNT), 0)
               FROM (SELECT CASE WHEN SELL_AMOUNT > BUY_AMOUNT THEN SELL_AMOUNT - BUY_AMOUNT END AS AMOUNT
                     FROM (SELECT CL_CODE, DEAL_DATE,
                                  NVL(SUM(DECODE(DEAL_CODE, '0', CL_TOTAL, 0)), 0) AS BUY_AMOUNT,
                                  NVL(SUM(DECODE(DEAL_CODE, '1', CL_TOTAL, 0)), 0) AS SELL_AMOUNT
                           FROM DENPASAR.REGULAR A
                           WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'), -2)
                                                 AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'),  0)
                             AND A.BOARD          != 'NG'
                             AND A.CL_INVOICE_DUE  > TO_DATE(:genDate,'yyyy-mm-dd')
                             AND A.CL_CODE NOT LIKE '17%'
                             AND A.CL_CODE NOT LIKE '11%'
                             AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')
                           GROUP BY CL_CODE, DEAL_DATE)
                     WHERE SELL_AMOUNT > BUY_AMOUNT)
           )
           +
           (
               SELECT NVL(SUM(CL_TOTAL), 0)
               FROM DENPASAR.REGULAR A
               WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'), -2)
                                     AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'),  0)
                 AND A.BOARD          = 'NG'
                 AND A.CL_INVOICE_DUE > TO_DATE(:genDate,'yyyy-mm-dd')
                 AND A.CL_CODE NOT LIKE '17%'
                 AND A.CL_CODE NOT LIKE '11%'
                 AND A.DEAL_CODE      = '1'
                 AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')
           )
           AS TOTAL_AMOUNT
    FROM DUAL
)
GROUP BY BOARD
ORDER BY CASE BOARD WHEN 'TOTAL AMT' THEN 2 ELSE 1 END, BOARD;