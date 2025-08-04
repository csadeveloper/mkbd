package com.ciptadana.mkbd_gen.database.oracle.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

@Slf4j
@Component
public class DataTransactionCustodyRepository {

    @Autowired
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private static final String TRIAL_BALANCE = "{CALL MKBD.PROC_TRIALBALANCE(?, ?, ?)}";
    private static final String CUSTODY_KSIE = "{CALL MKBD.PROC_CUSTODY_KSIE(?)}";
    private static final String CUSTODY_HUTANGPIUTANG = "{CALL MKBD.PROC_CUSTODY_HUTANGPIUTANG(?)}";
    private static final String CUSTODY_PORTFOLIOEFEK = "{CALL MKBD.PROC_CUSTODY_PORTFOLIOEFEK(?)}";
    private static final String CUSTODY_HARGAPEROLEHAN = "{CALL MKBD.PROC_CUSTODY_HARGAPEROLEHAN(?,?)}";
    private static final String CUSTODY_REKENINGEFEK = "{CALL MKBD.PROC_CUSTODY_REKENINGEFEK(?)}";
    private static final String CUSTODY_CUSTODIAN = "{CALL MKBD.PROC_CUSTODY_CUSTODIAN(?)}";
    private static final String CUSTODY_OTHER = "{CALL MKBD.PROC_CUSTODY_OTHER(?)}";
    private static final String CUSTODY_CLOSING_PRICE = "{CALL MKBD.PROC_CLOSING_PRICE(?)}";

    private static final String MODE = "combine";

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_RETRY_DELAY = 1000; // 1 second
    private static final long MAX_RETRY_DELAY = 10000; // 10 seconds

    @Autowired
    public DataTransactionCustodyRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
        CompletableFuture<Void> trialFuture = asyncDataCustody.callTrialBalanceAsync(pDate);
        CompletableFuture<Void> kseiFuture = asyncDataCustody.callCustodyKseiAsync(pDate);
        CompletableFuture<Void> hutangPiutangFuture = asyncDataCustody.callHutangPiutangAsync(pDate);
        CompletableFuture<Void> portfolioEfekFuture = asyncDataCustody.callPortfolioEfekAsync(pDate);
        CompletableFuture<Void> hpp1Future = asyncDataCustody.callHargaPerolehan1Async(pDate);
//        CompletableFuture<Void> hpp2Future = asyncDataCustody.callHargaPerolehan2Async(pDate);
        CompletableFuture<Void> rekeningEfekFuture = asyncDataCustody.callRekeningEfekAsync(pDate);
        CompletableFuture<Void> custodianFuture = asyncDataCustody.callCustodianAsync(pDate);
        CompletableFuture<Void> othersFuture = asyncDataCustody.callOthersAsync(pDate);
        CompletableFuture<Void> closingPriceFuture = asyncDataCustody.callClosingPriceAsync(pDate);
     */

    @Transactional
    public void truncateCustody() {
        jdbcTemplate.execute("TRUNCATE TABLE MKBD.CUSTODY");
    }

    @Transactional
    public void truncateAgingRecv() {
        jdbcTemplate.execute("TRUNCATE TABLE MKBD.AGINGRECV");
    }

    @Transactional
    public void truncateLedger() {
        jdbcTemplate.execute("TRUNCATE TABLE MKBD.LEDGER");
    }

    public boolean callTrialBalance(String pDate) {

        String result = "";
        try (

             Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(TRIAL_BALANCE)) {
                log.info("--Trial balance start");
                callableStatement.setQueryTimeout(300);
                callableStatement.setString(1, pDate);
                callableStatement.setString(2, "");
                callableStatement.registerOutParameter(3, Types.VARCHAR);
                callableStatement.execute();
                result = callableStatement.getString(3);

            log.info("--Trial balance result: {}", result);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.TRIALBALANCE", e);
        }
        return true;
    }

    public boolean callCustodyKsei(String pDate) {
        try (

        Connection connection = dataSource.getConnection();
        CallableStatement callableStatement = connection.prepareCall(CUSTODY_KSIE)) {
        log.info("--KSEI Start");
        callableStatement.setQueryTimeout(300);
        callableStatement.setString(1, pDate);
        callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

        log.info("--KSEI Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_KSIE", e);
        }
        return true;
    }

    public boolean callCustodyHutangPiutang(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_HUTANGPIUTANG)) {
            log.info("--HutangPiutang Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--HutangPiutang Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_HUTANGPIUTANG", e);
        }
        return true;
    }

    public boolean callCustodyPortfolioEfek(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_PORTFOLIOEFEK)) {
            log.info("--Portfolio Efek Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Portfolio Efek Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_PORTFOLIOEFEK", e);
        }
        return true;
    }

//    public void callCustodyHargaPerolehan1(String pDate, int maxRetries) throws InterruptedException {
//
//        int retryCount = 0;
//
//        while (retryCount < maxRetries) {
//            try (
//
//                Connection connection = dataSource.getConnection();
//                CallableStatement callableStatement = connection.prepareCall(CUSTODY_HARGAPEROLEHAN)) {
//                callableStatement.setQueryTimeout(300);
//                callableStatement.setString(1, pDate);
//                callableStatement.setInt(2, 631);
//                callableStatement.execute();
//
//                if (!connection.getAutoCommit())
//                    connection.commit();
//
//                callCustodyHargaPerolehan2(pDate);
//
//
//
//            } catch (SQLException e) {
//                if (e.getErrorCode() == 54) { // ORA-00054
//                    retryCount++;
//                    if (retryCount >= maxRetries) {
//                        throw new RuntimeException("Failed after " + maxRetries + " retries", e);
//                    }
//                    Thread.sleep((long) (Math.pow(2, retryCount) * 1000));
//                } else {
//                    throw new RuntimeException("SQL error", e);
//                }
//            }catch(Exception e){
//                throw new RuntimeException("Failed to execute MKBD.CUSTODY_HARGAPEROLEHAN1", e);
//            }
//        }
//    }

    public boolean callCustodyHargaPerolehan1(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_HARGAPEROLEHAN)) {
            log.info("--Harga Perolehan1 Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.setInt(2, 631);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Harga Perolehan1 Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_HARGAPEROLEHAN2", e);
        }
        return true;
    }

    public boolean callCustodyHargaPerolehan2(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_HARGAPEROLEHAN)) {
            log.info("--Harga Perolehan2 Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.setInt(2, 632);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

        log.info("--Harga Perolehan2 Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_HARGAPEROLEHAN2", e);
        }
        return true;
    }

    public boolean callCustodyRekeningEfek(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_REKENINGEFEK)) {
            log.info("--Rekening Efek Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

        log.info("--Rekening Efek Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_REKENINGEFEK", e);
        }
        return true;
    }

    public boolean callCustodyCustodian(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_CUSTODIAN)) {
            log.info("--Custodian Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

        log.info("--Custodian Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_CUSTODIAN", e);
        }
        return true;
    }

    public boolean callCustodyOthers(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(CUSTODY_OTHER)) {
            log.info("--Custodian Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

        log.info("--Custody Others Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_OTHER", e);
        }
        return true;
    }


    public boolean callCustodyClosingPrice(String pDate) {
        int attempt = 0;
        long retryDelay = INITIAL_RETRY_DELAY;

        SQLException lastError = null;
        log.info("--Closing Price Start");

        while (attempt < MAX_RETRIES) {

            try (Connection connection = dataSource.getConnection();
                 CallableStatement callableStatement = connection.prepareCall(CUSTODY_CLOSING_PRICE)) {

                callableStatement.setQueryTimeout(300);
                callableStatement.setString(1, pDate);
                callableStatement.execute();

                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                log.info("--Closing Price Completed");
                return true;

            } catch (SQLException e) {
                lastError = e;
                attempt++;

                if (!isTransientError(e)) {
                    break;
                }

                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(retryDelay);
                        retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted during retry", ie);
                    }
                }
            }
        }

        throw new RuntimeException("Failed to execute MKBD.CUSTODY_CLOSING_PRICE after " +
                attempt + " attempts. Last error code: " +
                (lastError != null ? lastError.getErrorCode() : "unknown"), lastError);
    }

    private boolean isTransientError(SQLException e) {
        // ORA-03111 and other transient errors
        return e.getErrorCode() == 3111 ||
                e.getErrorCode() == 3135 || // broken connection
                e.getErrorCode() == 12571;  // TNS packet writer failure
    }

//    public void callCustodyClosingPrice(String pDate) {
//        try (
//
//            Connection connection = dataSource.getConnection();
//            CallableStatement callableStatement = connection.prepareCall(CUSTODY_CLOSING_PRICE)) {
//            callableStatement.setQueryTimeout(300);
//            callableStatement.setString(1, pDate);
//            callableStatement.execute();
//
//            if(!connection.getAutoCommit())
//                connection.commit();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to execute MKBD.CUSTODY_CLOSING_PRICE", e);
//        }
//    }
}
