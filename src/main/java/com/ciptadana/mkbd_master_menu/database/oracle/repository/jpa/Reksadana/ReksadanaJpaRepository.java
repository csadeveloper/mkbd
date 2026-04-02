package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Reksadana;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaIsinResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaRiskResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReksadanaJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT MKBD.TRX_RDAUM.ROWID AS ID,
             MKBD.TRX_RDAUM.*,
             MKBD.RISK_LIMIT.HAIRCUT,
             MKBD.RISK_LIMIT.LIMITPCT
            FROM MKBD.TRX_RDAUM,
             MKBD.RISK_LIMIT
            WHERE MKBD.TRX_RDAUM.TYPE = MKBD.RISK_LIMIT.TYPE
             AND MKBD.TRX_RDAUM.DEAL_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY MKBD.TRX_RDAUM.NSHARE_NAME, MKBD.TRX_RDAUM.DEAL_DATE
            """, nativeQuery = true)
    List<ReksadanaListResponse> findReksadanaList(
            @Param("date") String date
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.RISK_LIMIT
            """, nativeQuery = true)
    List<ReksadanaRiskResponse> findReksadanaRisk(
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.ISINCODE
            WHERE ISINTYPE = 'REKSADANA'
            ORDER BY ISINCODE
            """, nativeQuery = true)
    List<ReksadanaIsinResponse> findReksadanaIsin(
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.ISINCODE
            WHERE ISINTYPE = 'REKSADANA'
            AND ISINCODE LIKE :prefix || '%'
            ORDER BY ISINCODE
            """, nativeQuery = true)
    List<ReksadanaIsinResponse> findReksadanaIsin(
            @Param("prefix") String prefix
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.ISINCODE
            WHERE ISINCODE = :isincode
            """, nativeQuery = true)
    List<ReksadanaIsinResponse> findReksadanaIsinLoad(
            @Param("isincode") String isincode
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_RDAUM
            (RECDATE, DEAL_DATE, TYPE, NSHARE_NAME, AMOUNT_UP,
            LAST_NAB, TOTAL_NAB_MI, AFFILIATED)
            VALUES (SYSDATE, TO_DATE(:dealDate, 'YYYY-MM-DD'), :type,
            :nshareName, :amountUp, :lastNab, :totalNabMi, :affiliated)
            """, nativeQuery = true)
    void insertReksadana(
            @Param("dealDate") String dealDate,
            @Param("type") String type,
            @Param("nshareName") String nshareName,
            @Param("amountUp") BigDecimal amountUp,
            @Param("lastNab") BigDecimal lastNab,
            @Param("totalNabMi") BigDecimal totalNabMi,
            @Param("affiliated") String affiliated
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_RDAUM
            SET DEAL_DATE = TO_DATE(:dealDate, 'YYYY-MM-DD'),
                TYPE = :type,
                NSHARE_NAME = :nshareName,
                AMOUNT_UP = :amountUp,
                LAST_NAB = :lastNab,
                TOTAL_NAB_MI = :totalNabMi,
                AFFILIATED = :affiliated
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateReksadana(
            @Param("rowid") String rowid,
            @Param("dealDate") String dealDate,
            @Param("type") String type,
            @Param("nshareName") String nshareName,
            @Param("amountUp") BigDecimal amountUp,
            @Param("lastNab") BigDecimal lastNab,
            @Param("totalNabMi") BigDecimal totalNabMi,
            @Param("affiliated") String affiliated
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_RDAUM
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteReksadana(@Param("rowid") String rowid);

    @Transactional
    @Query(value = """
            SELECT PARAMVAL FROM MKBD.PARAM WHERE CODE='RECDATE'
            """, nativeQuery = true)
    String getCurrentDate(
    );


}
