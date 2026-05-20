SELECT BANK_NAME AS DESCRIPTION, NCLIENT, SUM(AMOUNT)
                            FROM MKBD.RDI_BALANCE_HISTORICAL
                            WHERE NCLIENT IS NOT NULL
                             AND TRUNC(TRANS_DATE) = TO_DATE('2026-04-21', 'YYYY-MM-DD')
                             AND BANK_NAME IN ('BCA','CIMB')
GROUP BY BANK_NAME, NCLIENT

UNION ALL


SELECT 'TOTAL AMOUNT - 207' AS DESCRIPTION, NCLIENT, SUM(AMOUNT)
                            FROM MKBD.MKBD_CLIENT_POSITION AP
                                     INNER JOIN DENPASAR.CLIENT C ON AP.NCLIENT = C.CODE
                                     LEFT JOIN DENPASAR.CLIENT_PROPERTY CP ON AP.NCLIENT = CP.CLIENT_ID
                            WHERE AP.TRANS_FLAG = 207
                              AND AP.AMOUNT < 0
                              AND AP.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
                              AND (LENGTH(NVL(CP.BANK_ACC_NO_INVESTOR, '')) < 1 OR CP.BANK_ACC_NO_INVESTOR IS NULL)
                              AND C.CLIENT_TYPE IN (12, 14, 16, 33)
GROUP BY NCLIENT