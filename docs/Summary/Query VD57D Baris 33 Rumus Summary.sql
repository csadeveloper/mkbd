SELECT DECODE(TRANS_FLAG, '912', 'LKP - Payment transactions', TRANS_FLAG) AS TRANS_FLAG,
       NVL(SUM(TC.AMOUNTIDR), 0)                                    AS AMT
                              FROM MKBD.MKBD_FINAL_TRADES TC
                              WHERE TC.GEN_DATE = TO_DATE('2026-04-30', 'YYYY-MM-DD')
                                AND TC.SOURCE_ID = '641'
GROUP BY TRANS_FLAG