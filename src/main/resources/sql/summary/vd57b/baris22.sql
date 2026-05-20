SELECT DECODE(TRANS_FLAG,'833', 'HUTANGPIUTANG - Ledger Transaksi Institusi', TRANS_FLAG) AS TRANS_FLAG, NVL(SUM(TC.AMOUNTIDR), 0) AS AMT
                              FROM MKBD.MKBD_FINAL_TRADES TC
                              WHERE TC.GEN_DATE = TO_DATE(:genDate, 'YYYY-MM-DD')
                              AND TC.SOURCE_ID IN ('648')
GROUP BY TRANS_FLAG
