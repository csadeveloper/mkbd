package com.ciptadana.mkbd_master_menu.service.ProsesMkbd;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ProsesMkbd.ProsesMkbdResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Types;

@Slf4j
@Service
public class ProsesMkbdService {

    private static final String PLSQL = """
            DECLARE
                TRXDATE     VARCHAR2(10);
                PERIOD_DATE VARCHAR2(6);
            BEGIN
                SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD'),
                       TO_CHAR(SYSDATE, 'YYYYMM')
                  INTO TRXDATE, PERIOD_DATE
                  FROM DUAL;

                MKBD.XDM_PROCSUSPEND(TRXDATE, ?);
                MKBD.XDM_PROCUNSUSPEND(TRXDATE, ?);
                MKBD.PROC_CUSTODYAVGOBLCALC(PERIOD_DATE, ?);

                ? := TRXDATE;
                ? := PERIOD_DATE;
            END;
            """;

    private final JdbcTemplate jdbc;

    public ProsesMkbdService(@Qualifier("oracleDataSource") DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public ProsesMkbdResponse runProsesMkbd() {
        long t0 = System.currentTimeMillis();

        ProsesMkbdResponse response = jdbc.execute((ConnectionCallback<ProsesMkbdResponse>) connection -> {
            try (CallableStatement cs = connection.prepareCall(PLSQL)) {
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.registerOutParameter(2, Types.VARCHAR);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.registerOutParameter(5, Types.VARCHAR);
                cs.execute();

                return ProsesMkbdResponse.builder()
                        .procSuspendResult(cs.getString(1))
                        .procUnsuspendResult(cs.getString(2))
                        .procCustodyAvgOblCalcResult(cs.getString(3))
                        .trxDate(cs.getString(4))
                        .periodDate(cs.getString(5))
                        .elapsedMs(System.currentTimeMillis() - t0)
                        .build();
            }
        });

        log.info("Proses MKBD selesai dalam {} ms — trxDate={}, periodDate={}, suspend={}, unsuspend={}, custodyAvgObl={}",
                response.getElapsedMs(),
                response.getTrxDate(),
                response.getPeriodDate(),
                response.getProcSuspendResult(),
                response.getProcUnsuspendResult(),
                response.getProcCustodyAvgOblCalcResult());

        return response;
    }
}
