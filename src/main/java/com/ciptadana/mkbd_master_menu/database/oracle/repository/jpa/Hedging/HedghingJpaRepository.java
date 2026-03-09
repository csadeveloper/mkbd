package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Hedging;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingEfekResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HedghingJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT MKBD.TRX_HEDGING_HC.ROWID AS ID, MKBD.TRX_HEDGING_HC.*, MKBD.HAIRCUT.NAME
            FROM MKBD.TRX_HEDGING_HC,
             MKBD.HAIRCUT
            WHERE MKBD.TRX_HEDGING_HC.NSHARE = MKBD.HAIRCUT.CODE
             AND DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            """, nativeQuery = true)
    List<HedgingListResponse> findHedgingList(
            @Param("date") String date
    );

    @Transactional
    @Query(value = """
            SELECT * FROM MKBD.HAIRCUT WHERE CODE = :code
            """, nativeQuery = true)
    List<HedgingEfekResponse> findHedgingEfek(
            @Param("code") String code
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_HEDGING_HC
            (RECDATE, DUE_DATE, NSHARE, NOMINAL, NOMINAL_HEDGING,
            HEDGING_VAL, HC_HEDGING_VAL, HC_VAL)
            VALUES (SYSDATE, TO_DATE(:dueDate, 'DD-MM-YYYY'), :nshare,
            :nominal, :nominalHedging, :hedgingVal, :hcHedgingVal, :hcVal)
            """, nativeQuery = true)
    void insertHedging(
            @Param("dueDate") String dueDate,
            @Param("nshare") String nshare,
            @Param("nominal") BigDecimal nominal,
            @Param("nominalHedging") BigDecimal nominalHedging,
            @Param("hedgingVal") BigDecimal hedgingVal,
            @Param("hcHedgingVal") BigDecimal hcHedgingVal,
            @Param("hcVal") BigDecimal hcVal
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_HEDGING_HC
            SET DUE_DATE = TO_DATE(:dueDate, 'DD-MM-YYYY'),
                NSHARE = :nshare,
                NOMINAL = :nominal,
                NOMINAL_HEDGING = :nominalHedging,
                HEDGING_VAL = :hedgingVal,
                HC_HEDGING_VAL = :hcHedgingVal,
                HC_VAL = :hcVal
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateHedging(
            @Param("rowid") String rowid,
            @Param("dueDate") String dueDate,
            @Param("nshare") String nshare,
            @Param("nominal") BigDecimal nominal,
            @Param("nominalHedging") BigDecimal nominalHedging,
            @Param("hedgingVal") BigDecimal hedgingVal,
            @Param("hcHedgingVal") BigDecimal hcHedgingVal,
            @Param("hcVal") BigDecimal hcVal
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_HEDGING_HC
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteHedging(@Param("rowid") String rowid);

}
