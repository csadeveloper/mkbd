SELECT TRX_CODE,
       TRX_POST,
       NOTE,
       TRX_SESS,
       TRX_TYPE,
       BRK_COD2,
       INV_TYP2,
       BRK_COD1,
       INV_TYP1,
       STK_CODE,
       STK_VOLM,
       STK_PRIC,
       TRX_DATE,
       TRX_ORD2,
       TRX_ORD1,
       TRX_REF2,
       TRX_REF1,
       TRX_KOPUR,
       TRX_TIME,
       TRX_USER,
       TRADING_ID,
       SETTLEDATE,
       SETTLEMECH
FROM DENPASAR.EXCHANGE_TRANSACTION_DATA A
WHERE TRX_DATE <= TO_DATE(:genDate, 'yyyy-mm-dd')
  AND TRX_TYPE = 'NG'
  AND SETTLEDATE > TO_DATE(:genDate, 'yyyy-mm-dd')
  AND BRK_COD2 != BRK_COD1
  AND BRK_COD2 = 'KI'
