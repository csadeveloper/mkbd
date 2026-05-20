SELECT ACCOUNT, NVL(SUM(TL.AMOUNT), 0) AS AMT
                              FROM MKBD.MKBD_COMPILED_TRADES TL
                              WHERE TL.GEN_DATE = TO_DATE(:genDate, 'YYYY-MM-DD')
                                AND ACCOUNT = '103'
GROUP BY ACCOUNT 