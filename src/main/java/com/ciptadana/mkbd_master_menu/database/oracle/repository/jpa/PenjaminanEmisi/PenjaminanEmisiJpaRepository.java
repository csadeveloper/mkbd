package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PenjaminanEmisi;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanEmisi.PenjaminanEmisiListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PenjaminanEmisiJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_UNDERWRITER.*
            FROM MKBD.TRX_UNDERWRITER
            WHERE CONTRACT_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY COUNTER_PARTY, CONTRACT_DATE
            """, nativeQuery = true)
    List<PenjaminanEmisiListResponse> findPenjaminanEmisiList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_UNDERWRITER
            (RECDATE, CONTRACT_DATE, TYPE, COUNTER_PARTY, STATUS,
            PORTION_VALUE, HAIRCUT_EFEK, ABSORBED_VAL, CREDIT_GUARANTEE, RL)
            VALUES (SYSDATE, TO_DATE(:contractDate, 'YYYY-MM-DD'), :type,
            :counterParty, :status, :portionValue, :haircutEfek,
            :absorbedVal, :creditGuarantee, :rl)
            """, nativeQuery = true)
    void insertPenjaminanEmisi(
            @Param("contractDate") String contractDate,
            @Param("type") String type,
            @Param("counterParty") String counterParty,
            @Param("status") String status,
            @Param("portionValue") BigDecimal portionValue,
            @Param("haircutEfek") BigDecimal haircutEfek,
            @Param("absorbedVal") BigDecimal absorbedVal,
            @Param("creditGuarantee") BigDecimal creditGuarantee,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_UNDERWRITER
            SET CONTRACT_DATE = TO_DATE(:contractDate, 'YYYY-MM-DD'),
                TYPE = :type,
                COUNTER_PARTY = :counterParty,
                STATUS = :status,
                PORTION_VALUE = :portionValue,
                HAIRCUT_EFEK = :haircutEfek,
                ABSORBED_VAL = :absorbedVal,
                CREDIT_GUARANTEE = :creditGuarantee,
                RL = :rl
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updatePenjaminanEmisi(
            @Param("rowid") String rowid,
            @Param("contractDate") String contractDate,
            @Param("type") String type,
            @Param("counterParty") String counterParty,
            @Param("status") String status,
            @Param("portionValue") BigDecimal portionValue,
            @Param("haircutEfek") BigDecimal haircutEfek,
            @Param("absorbedVal") BigDecimal absorbedVal,
            @Param("creditGuarantee") BigDecimal creditGuarantee,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_UNDERWRITER
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deletePenjaminanEmisi(@Param("rowid") String rowid);

}
