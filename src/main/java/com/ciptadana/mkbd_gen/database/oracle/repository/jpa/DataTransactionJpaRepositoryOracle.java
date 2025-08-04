package com.ciptadana.mkbd_gen.database.oracle.repository.jpa;

import com.ciptadana.mkbd_gen.database.oracle.entity.NativeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

public interface DataTransactionJpaRepositoryOracle extends JpaRepository<NativeEntity, String> {

    @Query(value = "SELECT CLIENTCODE ||'|'|| NAME KEY FROM CORE.RDI_BALANCE " +
                    "LEFT JOIN DENPASAR.CLIENT@BOS ON CLIENTCODE = CODE " +
                    "WHERE NOREK = :accountNo AND CLOSURED_BY IS NULL AND ROWNUM = 1 ", nativeQuery = true)
    String getClientCode(@Param("accountNo") String accountNo);


}
