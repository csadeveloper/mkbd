-- =========================================================================
-- MKBD Query Benchmark Script
-- =========================================================================
-- Tujuan : Dapetin actual runtime + plan execution buat 4 query representatif
--          sebelum decide arsitektur endpoint (parallel vs snapshot vs cache).
--
-- Cara pake (di SQL Developer / sqlplus):
--   1. Login pake user yang sama dengan service runtime
--      (pastikan grant ke MKBD.* dan DENPASAR.*@cammon udah ada)
--   2. Set serveroutput on
--      ALTER SESSION SET STATISTICS_LEVEL = ALL;
--   3. Run blok per query (Q1..Q4) satu per satu
--   4. Catet:
--        - "E-Time" / elapsed dari DBMS_XPLAN
--        - "Buffers" (logical reads) — proxy buat cost sebenernya
--        - "A-Rows" vs "E-Rows" — kalau jauh beda, statistik stale
--
-- NOTE: Hint /*+ gather_plan_statistics */ wajib supaya DISPLAY_CURSOR
--       balikin ALLSTATS LAST yg ada actual numbers.
-- =========================================================================

ALTER SESSION SET STATISTICS_LEVEL = ALL;
SET SERVEROUTPUT ON
SET LINESIZE 200
SET PAGESIZE 100

-- =========================================================================
-- Q1: COA Detail (paling berat — 200+ baris CTE, multiple aggregation,
--     dblink @cammon, full ledger scan)
-- =========================================================================
PROMPT ============================================================
PROMPT Q1: Sum COA ALL Detail
PROMPT ============================================================

SELECT /*+ gather_plan_statistics */ COUNT(*) FROM (
    WITH BASE_ACCOUNTS AS (
        SELECT ACCOUNT, 1 AS CURRENCY
        FROM DENPASAR.LEDGER_HISTORICAL@cammon
        WHERE TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21', 'yyyy-mm-dd'), 'yyyymm')
          AND TRANS_DATE  <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
          AND GEN_DATE    =  TO_DATE('2026-04-21', 'yyyy-mm-dd')
          AND ACCOUNT LIKE DECODE(NULL, NULL, ACCOUNT, NULL || '%')
        GROUP BY ACCOUNT
    ),
    ACCOUNT_CATEGORIES AS (
        SELECT BA.ACCOUNT, BA.CURRENCY,
               CASE WHEN AT.CATEGORY IN (1, 4)    THEN 0
                    WHEN AT.CATEGORY IN (2, 3, 5) THEN 1 ELSE NULL END AS SUBACCOUNT_TYPE
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
        SELECT AWC.ACCOUNT, AWC.CURRENCY, AWC.SUBACCOUNT_TYPE AS ACCT_SUBACCOUNT_TYPE,
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
        WHERE L.TRANS_PERIOD = TO_CHAR(TO_DATE('2026-04-21','yyyy-mm-dd'),'yyyymm')
          AND L.GEN_DATE     = TO_DATE('2026-04-21','yyyy-mm-dd')
          AND L.TRANS_DATE  <= TO_DATE('2026-04-21','yyyy-mm-dd')
          AND ( L.TRANS_CURRENT = 0
             OR (L.TRANS_CURRENT = 1
                 AND ((AWF.ACCT_SUBACCOUNT_TYPE IN (0,1) AND AWF.TRANS_FLAG = 0)
                   OR (AWF.TRANS_FLAG = 1 AND L.TRANS_DATE = TO_DATE('2026-04-21','yyyy-mm-dd'))
                   OR (AWF.ACCT_SUBACCOUNT_TYPE = 9))))
    )
    SELECT * FROM LEDGER_FILTERED
);

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(FORMAT => 'ALLSTATS LAST +COST +BYTES'));

-- =========================================================================
-- Q2: VD51 Baris 30 Detail (representative: hits MKBD_COMPILED_JOURNAL,
--     CTE + self-join, function call CALCULATE_DUEDATE)
-- =========================================================================
PROMPT ============================================================
PROMPT Q2: VD51 Baris 30 Detail
PROMPT ============================================================

SELECT /*+ gather_plan_statistics */ COUNT(*) FROM (
    WITH DATE_NET AS (
        SELECT L.TRANS_DATE,
               SUM(CASE L.TRANS_FLAG WHEN 911 THEN L.AMOUNT WHEN 912 THEN -L.AMOUNT END) AS MARKET_VALUE
        FROM MKBD.MKBD_COMPILED_JOURNAL L
        WHERE L.TRANS_FLAG IN (911, 912)
          AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
          AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE('2026-04-21', -2)
                              AND DENPASAR.CALCULATE_DUEDATE('2026-04-21', 0)
        GROUP BY L.TRANS_DATE)
    SELECT L.TRANS_FLAG, L.TRANS_MODULE, L.TRANS_NO, L.TRANS_DATE, L.TRANS_DUEDATE,
           L.ACCOUNT, L.SUBACCOUNT, L.DESCRIPTION,
           CASE L.TRANS_FLAG WHEN 911 THEN L.AMOUNT WHEN 912 THEN -L.AMOUNT END AS SIGNED_AMOUNT
    FROM MKBD.MKBD_COMPILED_JOURNAL L
             JOIN DATE_NET D ON D.TRANS_DATE = L.TRANS_DATE
    WHERE L.TRANS_FLAG IN (911, 912)
      AND L.GEN_DATE = TO_DATE('2026-04-21', 'yyyy-mm-dd')
      AND L.TRANS_DATE BETWEEN DENPASAR.CALCULATE_DUEDATE('2026-04-21', -2)
                          AND DENPASAR.CALCULATE_DUEDATE('2026-04-21', 0)
      AND D.MARKET_VALUE > 0
);

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(FORMAT => 'ALLSTATS LAST +COST +BYTES'));

-- =========================================================================
-- Q3: VD51 Baris 71-81 Detail (representative: MKBD_COMPILED_TRADES join
--     ke HAIRCUT + CLOSING_PRICE — banyak baris di klien aktif)
-- =========================================================================
PROMPT ============================================================
PROMPT Q3: VD51 Baris 71-81 Detail (Trades + Haircut + ClosingPrice)
PROMPT ============================================================

SELECT /*+ gather_plan_statistics */ COUNT(*) FROM (
    SELECT TO_DATE('2026-04-21', 'yyyy-mm-dd') AS RECDATE,
           CU.NCLIENT, CU.NSHARE, CU.TRANS_PERIOD, CU.TRANS_DATE, CU.GEN_DATE,
           CU.PRICE, CU.QUANTITY,
           CASE WHEN CU.TRANS_PERIOD = '622' THEN CU.PRICE ELSE NVL(CP.CLOSINGPRICE, 1) END AS CLOSING_PRICE,
           CU.QUANTITY * CP.CLOSINGPRICE AS MVAL,
           CASE WHEN H.CODE IS NULL THEN 85
                WHEN NVL(H.MKBD, 0) <= 0 THEN 0
                ELSE H.MKBD END AS HAIRCUTMKBD
    FROM MKBD.MKBD_COMPILED_TRADES CU
    LEFT JOIN MKBD.HAIRCUT H ON CU.NSHARE = H.CODE
    LEFT JOIN DENPASAR.CLOSING_PRICE CP
           ON CU.NSHARE = CP.NSHARE
          AND CP.TANGGAL = TRUNC(TO_DATE('2026-04-21', 'yyyy-mm-dd'))
    WHERE CU.TRANS_PERIOD = '621'
      AND CU.TRANS_DATE  <= TO_DATE('2026-04-21', 'yyyy-mm-dd')
      AND CU.GEN_DATE     = TO_DATE('2026-04-21', 'yyyy-mm-dd')
      AND NVL(CP.CLOSINGPRICE, 0) > 0
);

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(FORMAT => 'ALLSTATS LAST +COST +BYTES'));

-- =========================================================================
-- Q4: VD57B Baris 47 Detail (representative: paling simple — single table
--     MKBD_FINAL_TRADES dengan filter SOURCE_ID)
-- =========================================================================
PROMPT ============================================================
PROMPT Q4: VD57B Baris 47 Detail (FinalTrades simple filter)
PROMPT ============================================================

SELECT /*+ gather_plan_statistics */ COUNT(*) FROM (
    SELECT TRANS_FLAG, TRANS_MODULE, TRANS_NO, TRANS_DATE, TRANS_DUE,
           ACCOUNT, NCLIENT, NSHARE, QUANTITY, CURRENCY, PRICE,
           AMOUNT, EXCHANGERATE, AMOUNTIDR
    FROM MKBD.MKBD_FINAL_TRADES TC
    WHERE TC.GEN_DATE = TO_DATE('2026-04-21', 'YYYY-MM-DD')
      AND TC.SOURCE_ID IN ('653')
);

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(FORMAT => 'ALLSTATS LAST +COST +BYTES'));

-- =========================================================================
-- Q5 (BONUS): Snapshot strategy validator
--   Test: kalau materialize LEDGER_FILTERED ke GTT, berapa cepat
--   query baris jadi? Ini buat validasi opsi A (per-request snapshot).
-- =========================================================================
PROMPT ============================================================
PROMPT Q5: Snapshot validation (materialize ke GTT, lalu query)
PROMPT ============================================================

-- One-time setup (jalanin sekali aja, comment kalau udah ada):
-- CREATE GLOBAL TEMPORARY TABLE MKBD.GTT_LEDGER_SNAPSHOT (
--     ACCOUNT VARCHAR2(50), SUBACCOUNT VARCHAR2(50),
--     LEDGER_SUBACCOUNT_TYPE NUMBER, NSHARE VARCHAR2(50),
--     BRANCH VARCHAR2(50), NFAKTUR VARCHAR2(50),
--     DESCRIPTION VARCHAR2(500), CURRENCY NUMBER,
--     TRANS_DATE DATE, TRANS_PERIOD VARCHAR2(10),
--     GEN_DATE DATE, TRANS_CURRENT NUMBER, AMOUNTIDR NUMBER,
--     ACCT_SUBACCOUNT_TYPE NUMBER, ACCOUNT_TRANS_FLAG NUMBER
-- ) ON COMMIT PRESERVE ROWS;

-- Step 1: Insert snapshot (timed)
-- INSERT /*+ append */ INTO MKBD.GTT_LEDGER_SNAPSHOT SELECT ... (LEDGER_FILTERED CTE);

-- Step 2: Run a "baris" query against GTT instead of base table
-- SELECT /*+ gather_plan_statistics */ ... FROM MKBD.GTT_LEDGER_SNAPSHOT WHERE ...;

-- Bandingkan elapsed Q1+Q2 (96 query) vs (Step1 + Step2 × 48)

-- =========================================================================
-- Hal yg dilihat di output DBMS_XPLAN:
-- -------------------------------------------------------------------------
--  E-Rows  : optimizer estimate (dari statistics)
--  A-Rows  : actual rows returned
--  A-Time  : actual elapsed per operation (HH:MM:SS.FF)
--  Buffers : logical reads (lower = better, ini proxy utama buat CPU cost)
--  Reads   : physical reads (kalau gede = data gak masuk buffer cache,
--            run kedua harusnya lebih cepet)
--
-- Kalau A-Rows >> E-Rows (10×+), statistics udah stale →
--   EXEC DBMS_STATS.GATHER_TABLE_STATS('MKBD','MKBD_COMPILED_JOURNAL');
--
-- Kalau ada "REMOTE" line dengan Buffers tinggi → @cammon dblink jadi
--   bottleneck, perlu materialize lokal (drives opsi A snapshot).
-- =========================================================================
