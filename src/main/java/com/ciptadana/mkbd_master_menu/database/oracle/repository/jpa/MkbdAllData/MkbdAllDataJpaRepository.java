package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MkbdAllData;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.CoaDetailRow;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.Vd51Baris30DetailRow;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.Vd51Baris30SummaryRow;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MkbdAllDataJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            WITH BASE_ACCOUNTS AS (
                SELECT ACCOUNT, 1 AS CURRENCY
                FROM DENPASAR.LEDGER_HISTORICAL@cammon
                WHERE TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate, 'yyyy-mm-dd'), 'yyyymm')
                  AND TRANS_DATE  <= TO_DATE(:genDate, 'yyyy-mm-dd')
                  AND GEN_DATE    =  TO_DATE(:genDate, 'yyyy-mm-dd')
                  AND ACCOUNT LIKE DECODE(NULL, NULL, ACCOUNT, NULL || '%')
                GROUP BY ACCOUNT
            ),
            ACCOUNT_CATEGORIES AS (
                SELECT BA.ACCOUNT, BA.CURRENCY,
                       CASE
                           WHEN AT.CATEGORY IN (1, 4)    THEN 0
                           WHEN AT.CATEGORY IN (2, 3, 5) THEN 1
                           ELSE NULL
                       END AS SUBACCOUNT_TYPE
                FROM BASE_ACCOUNTS BA
                LEFT JOIN DENPASAR.ACCOUNT      A  ON BA.ACCOUNT = A.CODE
                LEFT JOIN DENPASAR.ACCOUNT_TYPE AT ON A.ACCOUNT_TYPE = AT.ID AND AT.ID < 500
            ),
            ACCOUNT_WITH_CLIENTS AS (
                SELECT AC.ACCOUNT, AC.CURRENCY,
                       CASE WHEN VC.BUY IS NOT NULL THEN 9 ELSE AC.SUBACCOUNT_TYPE END AS SUBACCOUNT_TYPE
                FROM ACCOUNT_CATEGORIES AC
                LEFT JOIN DENPASAR.VIEWCLIENTTYPE VC ON AC.ACCOUNT = VC.BUY AND VC.TYPE <> 11
            ),
            ACCOUNT_WITH_FLAGS AS (
                SELECT AWC.ACCOUNT, AWC.CURRENCY,
                       AWC.SUBACCOUNT_TYPE AS ACCT_SUBACCOUNT_TYPE,
                       CASE WHEN A.CODE IS NOT NULL THEN 1 ELSE 0 END AS TRANS_FLAG
                FROM ACCOUNT_WITH_CLIENTS AWC
                LEFT JOIN DENPASAR.ACCOUNT A ON AWC.ACCOUNT = A.CODE AND A.ACCOUNT_TYPE = 109
            ),
            LEDGER_FILTERED AS (
                SELECT L.ACCOUNT, L.SUBACCOUNT, L.SUBACCOUNT_TYPE AS LEDGER_SUBACCOUNT_TYPE,
                       L.NSHARE, L.BRANCH, L.NFAKTUR, L.DESCRIPTION, L.CURRENCY,
                       L.TRANS_DATE, L.TRANS_PERIOD, L.GEN_DATE, L.TRANS_CURRENT, L.AMOUNTIDR,
                       AWF.ACCT_SUBACCOUNT_TYPE, AWF.TRANS_FLAG AS ACCOUNT_TRANS_FLAG
                FROM DENPASAR.LEDGER_HISTORICAL@cammon L
                JOIN ACCOUNT_WITH_FLAGS AWF ON L.ACCOUNT = AWF.ACCOUNT
                WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE(:genDate,'yyyy-mm-dd'),'yyyymm')
                  AND L.GEN_DATE     = TO_DATE(:genDate,'yyyy-mm-dd')
                  AND L.TRANS_DATE  <= TO_DATE(:genDate,'yyyy-mm-dd')
                  AND ( L.TRANS_CURRENT = 0
                     OR (L.TRANS_CURRENT = 1
                         AND ((AWF.ACCT_SUBACCOUNT_TYPE IN (0,1) AND AWF.TRANS_FLAG = 0)
                           OR (AWF.TRANS_FLAG = 1 AND L.TRANS_DATE = TO_DATE(:genDate,'yyyy-mm-dd'))
                           OR (AWF.ACCT_SUBACCOUNT_TYPE = 9))))
            ),
            SUBACCT_AGG AS (
                SELECT L.ACCOUNT, L.SUBACCOUNT, L.ACCT_SUBACCOUNT_TYPE, L.ACCOUNT_TRANS_FLAG,
                       SUM(CASE WHEN L.TRANS_CURRENT = 0 THEN L.AMOUNTIDR ELSE 0 END) AS OPEN_NET,
                       SUM(CASE WHEN L.TRANS_CURRENT = 1 AND L.AMOUNTIDR >= 0 THEN L.AMOUNTIDR  ELSE 0 END) AS CUR_DEBIT,
                       SUM(CASE WHEN L.TRANS_CURRENT = 1 AND L.AMOUNTIDR <  0 THEN -L.AMOUNTIDR ELSE 0 END) AS CUR_CREDIT,
                       SUM(L.AMOUNTIDR) AS SUBACCT_ALL_NET
                FROM LEDGER_FILTERED L
                GROUP BY L.ACCOUNT, L.SUBACCOUNT, L.ACCT_SUBACCOUNT_TYPE, L.ACCOUNT_TRANS_FLAG
            ),
            ACCT_AGG AS (
                SELECT ACCOUNT, MAX(ACCT_SUBACCOUNT_TYPE) AS ACCT_SUBACCOUNT_TYPE,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 0                       THEN OPEN_NET          ELSE 0 END) AS OPENBAL_DEBIT,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 1                       THEN -OPEN_NET         ELSE 0 END) AS OPENBAL_CREDIT,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 9 AND OPEN_NET > 0      THEN OPEN_NET          ELSE 0 END) AS OB9_DEBIT,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 9 AND OPEN_NET < 0      THEN -OPEN_NET         ELSE 0 END) AS OB9_CREDIT,
                       SUM(CUR_DEBIT)  AS DEBIT,
                       SUM(CUR_CREDIT) AS CREDIT,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 9 AND SUBACCT_ALL_NET > 0 THEN SUBACCT_ALL_NET  ELSE 0 END) AS ENDADJ_DEBIT,
                       SUM(CASE WHEN ACCT_SUBACCOUNT_TYPE = 9 AND SUBACCT_ALL_NET < 0 THEN -SUBACCT_ALL_NET ELSE 0 END) AS ENDADJ_CREDIT
                FROM SUBACCT_AGG
                GROUP BY ACCOUNT
            ),
            ACCT_AMT AS (
                SELECT AA.ACCOUNT, AA.ACCT_SUBACCOUNT_TYPE,
                       CASE
                           WHEN AA.ACCT_SUBACCOUNT_TYPE = 0 THEN AA.OPENBAL_DEBIT + AA.DEBIT - AA.CREDIT
                           WHEN AA.ACCT_SUBACCOUNT_TYPE = 9 THEN AA.OB9_DEBIT + AA.ENDADJ_DEBIT
                           ELSE 0
                       END AS RAW_ENDBAL_DEBIT,
                       CASE
                           WHEN AA.ACCT_SUBACCOUNT_TYPE = 1 THEN AA.OPENBAL_CREDIT + AA.CREDIT - AA.DEBIT
                           WHEN AA.ACCT_SUBACCOUNT_TYPE = 9 THEN AA.OB9_CREDIT + AA.ENDADJ_CREDIT
                           ELSE 0
                       END AS RAW_ENDBAL_CREDIT
                FROM ACCT_AGG AA
            ),
            ACCT_FINAL AS (
                SELECT A.ACCOUNT, A.ACCT_SUBACCOUNT_TYPE,
                       CASE
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 0 AND A.RAW_ENDBAL_DEBIT  < 0 THEN 0
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 0                               THEN A.RAW_ENDBAL_DEBIT
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 1 AND A.RAW_ENDBAL_CREDIT < 0 THEN ABS(A.RAW_ENDBAL_CREDIT)
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 9                               THEN A.RAW_ENDBAL_DEBIT
                           ELSE 0
                       END AS ENDBAL_DEBIT,
                       CASE
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 0 AND A.RAW_ENDBAL_DEBIT  < 0 THEN ABS(A.RAW_ENDBAL_DEBIT)
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 1 AND A.RAW_ENDBAL_CREDIT < 0 THEN 0
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 1                               THEN A.RAW_ENDBAL_CREDIT
                           WHEN A.ACCT_SUBACCOUNT_TYPE = 9                               THEN A.RAW_ENDBAL_CREDIT
                           ELSE 0
                       END AS ENDBAL_CREDIT
                FROM ACCT_AMT A
            ),
            ACCT_WITH_AMT AS (
                SELECT F.ACCOUNT, F.ACCT_SUBACCOUNT_TYPE,
                       NVL(
                           CASE SUBSTR(F.ACCOUNT, 1, 1)
                               WHEN '3' THEN  F.ENDBAL_DEBIT + F.ENDBAL_CREDIT
                               WHEN '4' THEN  F.ENDBAL_CREDIT - F.ENDBAL_DEBIT
                               WHEN '5' THEN (F.ENDBAL_DEBIT - F.ENDBAL_CREDIT) * -1
                               WHEN '6' THEN  F.ENDBAL_CREDIT - F.ENDBAL_DEBIT
                               WHEN '7' THEN (F.ENDBAL_DEBIT + F.ENDBAL_CREDIT) * -1
                               ELSE           F.ENDBAL_DEBIT + F.ENDBAL_CREDIT
                           END
                       , 0) AS ACCOUNT_AMT
                FROM ACCT_FINAL F
            ),
            PER_ROW AS (
                SELECT M.VD_SEKURITAS_CAT, M.VD_LINE_NO,
                       LF.ACCOUNT, ACC.NAME AS ACCOUNT_NAME, LF.NSHARE,
                       LF.SUBACCOUNT, LF.LEDGER_SUBACCOUNT_TYPE, LF.ACCT_SUBACCOUNT_TYPE,
                       LF.BRANCH, LF.NFAKTUR, LF.DESCRIPTION, LF.CURRENCY,
                       LF.TRANS_DATE, LF.TRANS_CURRENT, LF.AMOUNTIDR, AWA.ACCOUNT_AMT,
                       CASE
                           WHEN SUM(ABS(LF.AMOUNTIDR)) OVER (PARTITION BY LF.ACCOUNT) = 0 THEN 0
                           ELSE ROUND(
                                   AWA.ACCOUNT_AMT * ABS(LF.AMOUNTIDR)
                                   / SUM(ABS(LF.AMOUNTIDR)) OVER (PARTITION BY LF.ACCOUNT),
                                   2)
                       END AS RAW_CONTRIB,
                       ROW_NUMBER() OVER (
                           PARTITION BY LF.ACCOUNT
                           ORDER BY ABS(LF.AMOUNTIDR) DESC, LF.TRANS_DATE, LF.SUBACCOUNT, LF.NFAKTUR
                       ) AS RN
                FROM LEDGER_FILTERED LF
                JOIN ACCT_WITH_AMT AWA ON LF.ACCOUNT = AWA.ACCOUNT
                JOIN MKBD.MKBD_COA_MAPPER M
                     ON LF.ACCOUNT     = M.ACCOUNT
                    AND M.ACCOUNT_TYPE = '1'
                    AND M.IS_ENABLED   = 'Y'
                LEFT JOIN DENPASAR.ACCOUNT ACC ON LF.ACCOUNT = ACC.CODE
            )
            SELECT
                VD_SEKURITAS_CAT       AS vdSekuritasCat,
                VD_LINE_NO             AS vdLineNo,
                ACCOUNT                AS account,
                ACCOUNT_NAME           AS accountName,
                NSHARE                 AS nshare,
                SUBACCOUNT             AS subaccount,
                LEDGER_SUBACCOUNT_TYPE AS ledgerSubaccountType,
                ACCT_SUBACCOUNT_TYPE   AS acctSubaccountType,
                BRANCH                 AS branch,
                NFAKTUR                AS nfaktur,
                DESCRIPTION            AS description,
                CURRENCY               AS currency,
                TRANS_DATE             AS transDate,
                TRANS_CURRENT          AS transCurrent,
                AMOUNTIDR              AS amountidr,
                ABS(AMOUNTIDR)         AS absAmountidr,
                CASE
                    WHEN RN = 1 THEN
                         ROUND(ACCOUNT_AMT, 2)
                         - SUM(RAW_CONTRIB) OVER (PARTITION BY ACCOUNT)
                         + RAW_CONTRIB
                    ELSE RAW_CONTRIB
                END                    AS amountContrib
            FROM PER_ROW
            ORDER BY VD_SEKURITAS_CAT, VD_LINE_NO, ACCOUNT, SUBACCOUNT, TRANS_CURRENT, TRANS_DATE
            """, nativeQuery = true)
    List<CoaDetailRow> findCoaDetail(@Param("genDate") String genDate);

    @Transactional
    @Query(value = """
            WITH DATE_NET AS (
                SELECT L.TRANS_DATE,
                       SUM(CASE L.TRANS_FLAG WHEN 911 THEN L.AMOUNT WHEN 912 THEN -L.AMOUNT END) AS MARKET_VALUE
                FROM MKBD.MKBD_COMPILED_JOURNAL L
                WHERE L.TRANS_FLAG IN (911, 912)
                  AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                  AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                                      AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
                GROUP BY L.TRANS_DATE
            )
            SELECT
                L.TRANS_FLAG     AS transFlag,
                L.TRANS_MODULE   AS transModule,
                L.TRANS_NO       AS transNo,
                L.TRANS_DATE     AS transDate,
                L.TRANS_DUEDATE  AS transDuedate,
                L.ACCOUNT        AS account,
                L.SUBACCOUNT     AS subaccount,
                L.DESCRIPTION    AS description,
                CASE L.TRANS_FLAG WHEN 911 THEN L.AMOUNT WHEN 912 THEN -L.AMOUNT END AS signedAmount
            FROM MKBD.MKBD_COMPILED_JOURNAL L
                     JOIN DATE_NET D ON D.TRANS_DATE = L.TRANS_DATE
            WHERE L.TRANS_FLAG IN (911, 912)
              AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
              AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                                  AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
              AND D.MARKET_VALUE > 0
            ORDER BY L.TRANS_DATE, L.TRANS_FLAG
            """, nativeQuery = true)
    List<Vd51Baris30DetailRow> findVd51Baris30Detail(@Param("genDate") String genDate);

    @Transactional
    @Query(value = """
            WITH DATE_NET AS (
                SELECT L.TRANS_DATE,
                       SUM(CASE L.TRANS_FLAG WHEN 911 THEN L.AMOUNT WHEN 912 THEN -L.AMOUNT END) AS MARKET_VALUE
                FROM MKBD.MKBD_COMPILED_JOURNAL L
                WHERE L.TRANS_FLAG IN (911, 912)
                  AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                  AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                                      AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
                GROUP BY L.TRANS_DATE),
                 FLAG_AGG AS (
                    SELECT L.TRANS_FLAG,
                           SUM(L.AMOUNT) AS TOTAL_AMOUNT,
                           COUNT(*)      AS ROW_COUNT
                    FROM MKBD.MKBD_COMPILED_JOURNAL L
                             JOIN DATE_NET D ON D.TRANS_DATE = L.TRANS_DATE
                    WHERE L.TRANS_FLAG IN (911, 912)
                      AND L.GEN_DATE = TO_DATE(:genDate, 'yyyy-mm-dd')
                      AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE(:genDate, -2)
                                          AND DENPASAR.CALCULATE_DUEDATE(:genDate, 0)
                      AND D.MARKET_VALUE > 0
                    GROUP BY L.TRANS_FLAG)
            SELECT
                TRANS_FLAG    AS transFlag,
                DESCRIPTION   AS description,
                TOTAL_AMOUNT  AS totalAmount
            FROM (
                SELECT TRANS_FLAG,
                       DECODE(TO_CHAR(TRANS_FLAG), 911, 'Receipt transactions', 912, 'Payment transactions') AS DESCRIPTION,
                       TOTAL_AMOUNT,
                       ROW_COUNT,
                       1 AS SORT_ORDER
                FROM FLAG_AGG
                UNION ALL
                SELECT 0 AS TRANS_FLAG,
                       'NET (Receipt transactions - Payment transactions)' AS DESCRIPTION,
                       SUM(CASE TRANS_FLAG WHEN 911 THEN TOTAL_AMOUNT WHEN 912 THEN -TOTAL_AMOUNT END) AS TOTAL_AMOUNT,
                       SUM(ROW_COUNT) AS ROW_COUNT,
                       2 AS SORT_ORDER
                FROM FLAG_AGG
                ORDER BY SORT_ORDER, TRANS_FLAG)
            """, nativeQuery = true)
    List<Vd51Baris30SummaryRow> findVd51Baris30Summary(@Param("genDate") String genDate);
}
