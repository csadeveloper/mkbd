package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.TransaksiValas;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.TransaksiValas.TransaksiValasListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransaksiValasJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_VALAS.*
            FROM MKBD.TRX_VALAS
            WHERE DEAL_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY CURRENCY_TRX, DEAL_DATE
            """, nativeQuery = true)
    List<TransaksiValasListResponse> findTransaksiValasList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_VALAS
            (RECDATE, DEAL_DATE, TYPE_TRX, CURRENCY_TRX,
            TRX_VAL, UNREALIZE_PL, RL)
            VALUES (SYSDATE, TO_DATE(:dealDate, 'DD-MM-YYYY'), :typeTrx,
            :currencyTrx, :trxVal, :unrealizePl, :rl)
            """, nativeQuery = true)
    void insertTransaksiValas(
            @Param("dealDate") String dealDate,
            @Param("typeTrx") String typeTrx,
            @Param("currencyTrx") String currencyTrx,
            @Param("trxVal") BigDecimal trxVal,
            @Param("unrealizePl") BigDecimal unrealizePl,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_VALAS
            SET DEAL_DATE = TO_DATE(:dealDate, 'DD-MM-YYYY'),
                TYPE_TRX = :typeTrx,
                CURRENCY_TRX = :currencyTrx,
                TRX_VAL = :trxVal,
                UNREALIZE_PL = :unrealizePl,
                RL = :rl
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateTransaksiValas(
            @Param("rowid") String rowid,
            @Param("dealDate") String dealDate,
            @Param("typeTrx") String typeTrx,
            @Param("currencyTrx") String currencyTrx,
            @Param("trxVal") BigDecimal trxVal,
            @Param("unrealizePl") BigDecimal unrealizePl,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_VALAS
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteTransaksiValas(@Param("rowid") String rowid);

}
