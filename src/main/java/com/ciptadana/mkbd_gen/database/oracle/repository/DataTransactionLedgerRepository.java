package com.ciptadana.mkbd_gen.database.oracle.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class DataTransactionLedgerRepository {

    @Autowired
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private static final String LEDGER_KPEI = "{CALL MKBD.PROC_LEDGER_KPEI(?)}";
    private static final String LEDGER_HUTANGPIUTANG = "{CALL MKBD.PROC_LEDGER_HUTANGPIUTANG(?)}";
    private static final String LEDGER_OTHERACCOUNTS_RG = "{CALL MKBD.PROC_LEDGER_OTHERACCOUNTS_RG(?)}";
    private static final String LEDGER_OTHERACCOUNTS_NONRG = "{CALL MKBD.PROC_LEDGER_OTHERS_NONRG(?)}";
    private static final String LEDGER_PROC_GENERALLEDGER = "{CALL MKBD.PROC_GENERAL_LEDGER(?)}";
    private static final String LEDGER_PROCSUSPEND = "{CALL MKBD.XDM_PROCSUSPEND(?,?)}";
    private static final String LEDGER_PROCUNSUSPEND = "{CALL MKBD.XDM_PROCUNSUSPEND(?,?)}";
    private static final String LEDGER_CUSTODYAVGOBLCALC = "{CALL MKBD.PROC_CUSTODYAVGOBLCALC(?,?)}";

    @Autowired
    public DataTransactionLedgerRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void callLedgerKpei(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_KPEI)) {
            log.info("--KPEI Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--KPEI Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.CUSTODY_KSIE", e);
        }
    }

    public void callLedgerHutangPiutang(String pDate) {

        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_HUTANGPIUTANG)) {
            log.info("--Hutang Piutang Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Hutang Piutang Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_HUTANGPIUTANG", e);
        }
    }

    public void callLedgerOthersAccountRg(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_OTHERACCOUNTS_RG)) {
            log.info("--Others Account RG Start");
            callableStatement.setQueryTimeout(1200);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

//            Future<Boolean> future = Executors.newSingleThreadExecutor().submit(() -> {
//                callableStatement.execute();
//                return true;
//            });
//
//            try {
//                future.get(600, TimeUnit.SECONDS));
//            } catch (TimeoutException e) {
//                callableStatement.cancel();
//                throw new RuntimeException("Operation timed out after 10 minutes", e);
//            }

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Others Account RG Completed");
            callLedgerOthersAccountNonRg(pDate);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_OTHERACCOUNTS_RG", e);
        }
    }

    public void callLedgerOthersAccountNonRg(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_OTHERACCOUNTS_NONRG)) {
            log.info("--Others Account Non RG Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Others Account Non RG Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_OTHERACCOUNTS_NONRG", e);
        }
    }

    public void callLedgerGeneralLedger(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_PROC_GENERALLEDGER)) {
            log.info("--General Ledger Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--General Ledger Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_PROC_GENERALLEDGER", e);
        }
    }

    public void callLedgerProcSuspend(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_PROCSUSPEND)) {
            log.info("--Proc Suspend Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Proc Suspend Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_PROCSUSPEND", e);
        }
    }

    public void callLedgerProcUnSuspend(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_PROCUNSUSPEND)) {
            log.info("--Proc UnSuspend Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, pDate);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Proc UnSuspend Completed");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_PROCUNSUSPEND ", e);
        }
    }

    public void callLedgerCustodyAvgObl(String pDate) {
        try (

            Connection connection = dataSource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(LEDGER_CUSTODYAVGOBLCALC)) {
            log.info("--Average Obligation Start");
            callableStatement.setQueryTimeout(300);
            callableStatement.setString(1, "TO_CHAR("+pDate+", 'YYYYMM')");
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.execute();

            if(!connection.getAutoCommit())
                connection.commit();

            log.info("--Average Obligation Completed ");

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute MKBD.LEDGER_CUSTODYAVGOBLCALC", e);
        }
    }
}
