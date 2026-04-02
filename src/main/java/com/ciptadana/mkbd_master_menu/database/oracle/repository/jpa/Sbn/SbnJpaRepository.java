package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Sbn;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SbnJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_SBN.*
            FROM MKBD.TRX_SBN
            WHERE DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY NSHARE, DUE_DATE
            """, nativeQuery = true)
    List<SbnListResponse> findSbnList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_SBN
            (RECDATE, NSHARE, DUE_DATE, PRICE, NOMINAL, MARKET_VALUE,
            ACQUISITION_PRICE, AFFILIATED)
            VALUES (SYSDATE, :nshare, TO_DATE(:dueDate, 'DD-MM-YYYY'),
            :price, :nominal, :marketValue, :acquisitionPrice, :affiliated)
            """, nativeQuery = true)
    void insertSbn(
            @Param("nshare") String nshare,
            @Param("dueDate") String dueDate,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("marketValue") BigDecimal marketValue,
            @Param("acquisitionPrice") BigDecimal acquisitionPrice,
            @Param("affiliated") String affiliated
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_SBN
            SET NSHARE = :nshare,
                DUE_DATE = TO_DATE(:dueDate, 'DD-MM-YYYY'),
                PRICE = :price,
                NOMINAL = :nominal,
                MARKET_VALUE = :marketValue,
                ACQUISITION_PRICE = :acquisitionPrice,
                AFFILIATED = :affiliated
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateSbn(
            @Param("rowid") String rowid,
            @Param("nshare") String nshare,
            @Param("dueDate") String dueDate,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("marketValue") BigDecimal marketValue,
            @Param("acquisitionPrice") BigDecimal acquisitionPrice,
            @Param("affiliated") String affiliated
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_SBN
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteSbn(@Param("rowid") String rowid);

    @Transactional
    @Query(value = """
            SELECT NVL(SALDO, 0) AS EQUITY_VALUE
            FROM MKBD.MKBD_FINAL_DATA
            WHERE VD_SEKURITAS_CAT = 'VD52'
             AND VD_LINE_NO = 172
             AND GEN_DATE = (SELECT MAX(GEN_DATE)
             FROM MKBD.MKBD_FINAL_DATA
             WHERE VD_SEKURITAS_CAT = 'VD52'
             AND VD_LINE_NO = 172
             AND GEN_DATE < TO_DATE(:asOfDate, 'YYYY-MM-DD') )
            """, nativeQuery = true)
    BigDecimal findLastEquity(@Param("asOfDate") String asOfDate);

}
