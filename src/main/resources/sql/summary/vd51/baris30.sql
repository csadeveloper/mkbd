WITH DATE_NET AS (SELECT L.TRANS_DATE,
                         SUM(CASE L.TRANS_FLAG
                                 WHEN 911 THEN L.AMOUNT
                                 WHEN 912 THEN -L.AMOUNT
                             END) AS MARKET_VALUE
                  FROM MKBD.MKBD_COMPILED_JOURNAL L
                  WHERE L.TRANS_FLAG IN (911, 912)
                    AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                    AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                      AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
                  GROUP BY L.TRANS_DATE),
     FLAG_AGG AS (SELECT L.TRANS_FLAG,
                         SUM(L.AMOUNT) AS TOTAL_AMOUNT,
                         COUNT(*)      AS ROW_COUNT
                  FROM MKBD.MKBD_COMPILED_JOURNAL L
                           JOIN DATE_NET D ON D.TRANS_DATE = L.TRANS_DATE
                  WHERE L.TRANS_FLAG IN (911, 912)
                    AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                    AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                      AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
                    AND D.MARKET_VALUE > 0
                  GROUP BY L.TRANS_FLAG)
SELECT TRANS_FLAG, DESCRIPTION, TOTAL_AMOUNT
FROM (SELECT TRANS_FLAG,
             DECODE(TO_CHAR(TRANS_FLAG), 911, 'Receipt transactions', 912, 'Payment transactions') AS DESCRIPTION,
             TOTAL_AMOUNT,
             ROW_COUNT,
             1                                                                                     AS SORT_ORDER
      FROM FLAG_AGG
      UNION ALL
      SELECT 0                                                   AS TRANS_FLAG,
             'NET (Receipt transactions - Payment transactions)' AS DESCRIPTION,
             SUM(CASE TRANS_FLAG
                     WHEN 911 THEN TOTAL_AMOUNT
                     WHEN 912 THEN -TOTAL_AMOUNT END)            AS TOTAL_AMOUNT,
             SUM(ROW_COUNT)                                      AS ROW_COUNT,
             2                                                   AS SORT_ORDER
      FROM FLAG_AGG
      ORDER BY SORT_ORDER, TRANS_FLAG)