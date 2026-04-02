package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PenjaminanKorporasi;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanKorporasi.PenjaminanKorporasiListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PenjaminanKorporasiJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_GUARANTEE.*
            FROM MKBD.TRX_GUARANTEE
            WHERE DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY COUNTER_PARTY
            """, nativeQuery = true)
    List<PenjaminanKorporasiListResponse> findPenjaminanKorporasiList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_GUARANTEE
            (RECDATE, CONTRACT_DATE, COUNTER_PARTY, AFFILIATED,
            DETAILS, DAYS, DUE_DATE, ASSURANCE_VAL, RL)
            VALUES (SYSDATE, TO_DATE(:contractDate, 'DD-MM-YYYY'), :counterParty,
            :affiliated, :details, :days, TO_DATE(:dueDate, 'DD-MM-YYYY'),
            :assuranceVal, :rl)
            """, nativeQuery = true)
    void insertPenjaminanKorporasi(
            @Param("contractDate") String contractDate,
            @Param("counterParty") String counterParty,
            @Param("affiliated") String affiliated,
            @Param("details") String details,
            @Param("days") Integer days,
            @Param("dueDate") String dueDate,
            @Param("assuranceVal") BigDecimal assuranceVal,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_GUARANTEE
            SET CONTRACT_DATE = TO_DATE(:contractDate, 'DD-MM-YYYY'),
                COUNTER_PARTY = :counterParty,
                AFFILIATED = :affiliated,
                DETAILS = :details,
                DAYS = :days,
                DUE_DATE = TO_DATE(:dueDate, 'DD-MM-YYYY'),
                ASSURANCE_VAL = :assuranceVal,
                RL = :rl
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updatePenjaminanKorporasi(
            @Param("rowid") String rowid,
            @Param("contractDate") String contractDate,
            @Param("counterParty") String counterParty,
            @Param("affiliated") String affiliated,
            @Param("details") String details,
            @Param("days") Integer days,
            @Param("dueDate") String dueDate,
            @Param("assuranceVal") BigDecimal assuranceVal,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_GUARANTEE
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deletePenjaminanKorporasi(@Param("rowid") String rowid);

}
