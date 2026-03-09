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

}
