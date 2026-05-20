SELECT
    LINE_NO,
    HAIRCUT_RANGE,
    NVL(SUM(TPORT.MVAL), 0) AS AMT
FROM (
    SELECT TO_DATE('2026-04-21', 'yyyy-mm-dd') AS RECDATE,
           CU.NCLIENT,
           CU.NSHARE,
           CASE
               WHEN CU.TRANS_PERIOD = '622' THEN CU.PRICE
               ELSE NVL(CP.CLOSINGPRICE, 1)
           END                  AS CLOSING_PRICE,
           SUM(CU.QUANTITY)     AS QTY,
           SUM(CU.QUANTITY) * CP.CLOSINGPRICE AS MVAL,
           CASE
               WHEN H.CODE IS NULL THEN 85
               WHEN NVL(H.MKBD, 0) <= 0 THEN 0
               ELSE H.MKBD
           END                  AS HAIRCUTMKBD
    FROM MKBD.MKBD_COMPILED_TRADES CU
    LEFT JOIN MKBD.HAIRCUT H
           ON CU.NSHARE = H.CODE
    LEFT JOIN DENPASAR.CLOSING_PRICE CP
           ON CU.NSHARE = CP.NSHARE
          AND CP.TANGGAL = TRUNC(TO_DATE('2026-04-21', 'yyyy-mm-dd'))
    WHERE CU.TRANS_PERIOD = '621'
      AND CU.TRANS_DATE  <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
      AND CU.GEN_DATE     = TO_DATE('2026-04-21', 'yyyy-mm-dd')
    GROUP BY CU.NCLIENT, CU.NSHARE, CU.TRANS_PERIOD, CU.PRICE, CP.CLOSINGPRICE, H.CODE, H.MKBD
    HAVING NVL(CP.CLOSINGPRICE, 0) > 0
) TPORT
INNER JOIN DENPASAR.CLIENT      C  ON TPORT.NCLIENT = C.CODE
INNER JOIN DENPASAR.CLIENT_TYPE CT ON C.CLIENT_TYPE = CT.ID
INNER JOIN (
    SELECT 71 AS LINE_NO,   5 AS LO, 10  AS HI, '5-10'   AS HAIRCUT_RANGE FROM DUAL UNION ALL
    SELECT 72,             15,      20,       '15-20'                     FROM DUAL UNION ALL
    SELECT 73,             21,      25,       '21-25'                     FROM DUAL UNION ALL
    SELECT 74,             26,      30,       '26-30'                     FROM DUAL UNION ALL
    SELECT 75,             31,      35,       '31-35'                     FROM DUAL UNION ALL
    SELECT 76,             36,      40,       '36-40'                     FROM DUAL UNION ALL
    SELECT 77,             41,      45,       '41-45'                     FROM DUAL UNION ALL
    SELECT 78,             46,      54,       '46-54'                     FROM DUAL UNION ALL
    SELECT 79,             55,      84,       '55-84'                     FROM DUAL UNION ALL
    SELECT 80,             85,     100,       '85-100'                    FROM DUAL UNION ALL
    SELECT 81,            100,     100,       '100'                       FROM DUAL
) RANGES
    ON TPORT.HAIRCUTMKBD BETWEEN RANGES.LO AND RANGES.HI
GROUP BY LINE_NO, HAIRCUT_RANGE
ORDER BY LINE_NO;

