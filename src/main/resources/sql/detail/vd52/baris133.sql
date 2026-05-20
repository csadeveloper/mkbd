SELECT A.CL_CODE,
       B.NAME,
       A.DEAL_DATE,
       A.BOARD,
       A.AMOUNT
FROM (
    -- === Cabang 1: Non-NG Netting (SELL > BUY) ===
    SELECT CL_CODE,
           DEAL_DATE,
           'Non RG (Netting)' AS BOARD,
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

    UNION ALL

    -- === Cabang 2: NG Sell (DEAL_CODE = '1') ===
    SELECT CL_CODE,
           DEAL_DATE,
           BOARD,
           SUM(CL_TOTAL) AS AMOUNT
    FROM DENPASAR.REGULAR A
    WHERE A.DEAL_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'), -2)
                          AND DENPASAR.CALCULATE_DUEDATE(TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyy-mm-dd'),  0)
      AND A.BOARD          = 'NG'
      AND A.CL_INVOICE_DUE > TO_DATE(:genDate,'yyyy-mm-dd')
      AND A.CL_CODE NOT LIKE '17%'
      AND A.CL_CODE NOT LIKE '11%'
      AND A.DEAL_CODE      = '1'
      AND A.CL_CODE NOT IN (SELECT CODE FROM DENPASAR.CLIENT WHERE CLIENT_TYPE = '11')
    GROUP BY CL_CODE, DEAL_DATE, BOARD
) A
LEFT JOIN DENPASAR.CLIENT B ON B.CODE = A.CL_CODE
WHERE A.AMOUNT IS NOT NULL
ORDER BY A.BOARD, A.CL_CODE, A.DEAL_DATE;