SELECT NCLIENT, SUM(MARKET_VALUE) AS VAL
                               FROM MKBD.MKBD_PORTFOLIO_HAIRCUT SP
                               WHERE SP.RECDATE = TO_DATE(:genDate, 'YYYY-MM-DD')
                               GROUP BY NCLIENT

UNION ALL

SELECT NSHARE_NAME, SUM(TOTAL_NAB) AS VAL
                             FROM MKBD.RDAUM_CONCENTRATION_RISK
                             WHERE GEN_DATE = TO_DATE(:genDate, 'YYYY-MM-DD')
GROUP BY NSHARE_NAME