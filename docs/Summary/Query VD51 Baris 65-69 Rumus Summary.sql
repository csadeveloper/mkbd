SELECT NSHARE_NAME,
                              RATING,
                               SUM(MARKET_VALUE) AS AMT
                        FROM MKBD.MKBD_EBUS
                        WHERE RECDATE = TO_DATE('2026-04-08', 'yyyy-mm-dd')
GROUP BY NSHARE_NAME, RATING
ORDER BY NSHARE_NAME, RATING