SELECT S.ACCOUNT, SUM(S.ENDBAL_DEBIT) AS BALANCE
                            FROM MKBD.MAP_BANK MB
                                     INNER JOIN MKBD.MKBD_BALANCE_SUMMARY S ON S.ACCOUNT = MB.NCLIENT
                                     INNER JOIN DENPASAR.ACCOUNT ACC ON ACC.CODE = S.ACCOUNT
                            WHERE S.GEN_DATE = TO_DATE(:genDate, 'YYYY-MM-DD')
                              AND LENGTH(MB.NCLIENT) = 8
                              AND LENGTH(MB.BANK_NAME) = 3
                              AND MB.ISCLIENT = 'N'
                              AND ACC.ACCOUNT_TYPE = (SELECT ID FROM DENPASAR.ACCOUNT_TYPE WHERE NAME = 'BANK')
GROUP BY S.ACCOUNT


UNION ALL

SELECT 'Total Amount - 207', -SUM(ABS(AP.AMOUNT)) AS BALANCE
                            FROM MKBD.MKBD_CLIENT_POSITION AP
                                     INNER JOIN DENPASAR.CLIENT C ON AP.NCLIENT = C.CODE
                                     LEFT JOIN DENPASAR.CLIENT_PROPERTY CP ON AP.NCLIENT = CP.CLIENT_ID
                            WHERE AP.TRANS_FLAG = 207
                              AND AP.AMOUNT < 0
                              AND AP.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                              AND (LENGTH(NVL(CP.BANK_ACC_NO_INVESTOR, '')) < 1 OR CP.BANK_ACC_NO_INVESTOR IS NULL)
                              AND C.CLIENT_TYPE IN (12, 14, 16, 33)

