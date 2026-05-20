SELECT ACCOUNT,
       SUBACCOUNT_TYPE,
       SUBACCOUNT,
       BRANCH,
       CURRENCY,
       OPENBAL,
       DEBIT,
       CREDIT,
       ENDBAL,
       ENDBAL_DEBIT,
       ENDBAL_CREDIT,
       OPENBAL_DEBIT,
       OPENBAL_CREDIT
            FROM MKBD.MKBD_BALANCE_SUMMARY
            WHERE GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
              AND ACCOUNT = '20701001'
              AND LENGTH(ACCOUNT) = 8
              AND MKBD_FLAG = 0