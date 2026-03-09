package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Obligasi;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiBondNameResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiRatingResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ObligasiJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_OBLIGASI
            (RECDATE, DEAL_DATE, NSHARE_NAME, RATING, QUANTITY, PRICE,
            MARKET_VALUE, HAIRCUT_VALUE, HAIRCUT_AFTER, CONCERN_RISK,
            ACQUISITION_PRICE, AFFILIATED, SUKUK)
            VALUES (SYSDATE, TO_DATE(:dealDate, 'DD-MM-YYYY'), :nshareName, :rating,
            :quantity, :price, :marketValue, :haircutValue, :haircutAfter,
            :concernRisk, :acquisitionPrice, :affiliated, :sukuk)
            """, nativeQuery = true)
    void insertObligasi(
            @Param("dealDate") String dealDate,
            @Param("nshareName") String nshareName,
            @Param("rating") String rating,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("marketValue") BigDecimal marketValue,
            @Param("haircutValue") BigDecimal haircutValue,
            @Param("haircutAfter") BigDecimal haircutAfter,
            @Param("concernRisk") BigDecimal concernRisk,
            @Param("acquisitionPrice") BigDecimal acquisitionPrice,
            @Param("affiliated") Short affiliated,
            @Param("sukuk") Short sukuk
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_OBLIGASI
            SET DEAL_DATE = TO_DATE(:dealDate, 'DD-MM-YYYY'),
                NSHARE_NAME = :nshareName,
                RATING = :rating,
                QUANTITY = :quantity,
                PRICE = :price,
                MARKET_VALUE = :marketValue,
                HAIRCUT_VALUE = :haircutValue,
                HAIRCUT_AFTER = :haircutAfter,
                CONCERN_RISK = :concernRisk,
                ACQUISITION_PRICE = :acquisitionPrice,
                AFFILIATED = :affiliated,
                SUKUK = :sukuk
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateObligasi(
            @Param("rowid") String rowid,
            @Param("dealDate") String dealDate,
            @Param("nshareName") String nshareName,
            @Param("rating") String rating,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("marketValue") BigDecimal marketValue,
            @Param("haircutValue") BigDecimal haircutValue,
            @Param("haircutAfter") BigDecimal haircutAfter,
            @Param("concernRisk") BigDecimal concernRisk,
            @Param("acquisitionPrice") BigDecimal acquisitionPrice,
            @Param("affiliated") Short affiliated,
            @Param("sukuk") Short sukuk
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_OBLIGASI
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteObligasi(@Param("rowid") String rowid);

    @Transactional
    @Query(value = """
            SELECT MKBD.TRX_OBLIGASI.ROWID AS ID, MKBD.TRX_OBLIGASI.*, MKBD.RATING_HC.HAIRCUT
            FROM MKBD.TRX_OBLIGASI,
             MKBD.RATING_HC
            WHERE MKBD.TRX_OBLIGASI.RATING = MKBD.RATING_HC.RATING
             AND DEAL_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            """, nativeQuery = true)
    List<ObligasiListResponse> findObligasiList(
            @Param("date") String date
    );

    @Transactional
    @Query(value = """
             SELECT *
            FROM MKBD.RATING_HC
            """, nativeQuery = true)
    List<ObligasiRatingResponse> findObligasiRating(
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.RATING_HC
            WHERE RATING = :rating
            """, nativeQuery = true)
    ObligasiRatingResponse findObligasiAutomatedRating(
            @Param("rating") String rating
    );

    @Transactional
    @Query(value = """
            SELECT DENPASAR.SHARES.*, DENPASAR.COUNTRY.NAME AS COUNTRYNAME
            FROM DENPASAR.SHARES,
            	 DENPASAR.COUNTRY
            WHERE
            	SHARES.COUNTRY = COUNTRY.ID
            	AND SHARES.CURRENCY = '1'
            	AND SHARES.CODE = :code
            ORDER BY
            	SHARES.CODE,
            	SHARES.NAME
            """, nativeQuery = true)
    ObligasiBondNameResponse findObligasiBondName(
            @Param("code") String code
    );

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
             AND GEN_DATE < TO_DATE(:pdate, 'YYYY-MM-DD') )
            """, nativeQuery = true)
    BigDecimal findCalculationBusiness(
            @Param("pdate") String pdate
    );



}
