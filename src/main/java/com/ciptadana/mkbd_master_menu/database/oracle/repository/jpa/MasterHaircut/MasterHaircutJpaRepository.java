package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterHaircut;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PengaturanParameter.ApplicationParameterResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MasterHaircutJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.HAIRCUT
            ORDER BY CODE
            """, nativeQuery = true)
    List<HaircutResponse> findHaircutList(
    );

    @Transactional
    @Query(value = """
            SELECT PARAMVAL
            FROM MKBD.PARAM
            WHERE CODE = 'LAST_UPLOAD_HAIRCUT'
            """, nativeQuery = true)
    String findLastUpdateDates(
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM MKBD.HAIRCUT_HIST
            WHERE REC_DATE = TO_DATE(:date, 'YYYY-MM-DD')
            """, nativeQuery = true)
    void deleteHaircutHist(@Param("date") String date);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.HAIRCUT_HIST
            (REC_DATE, CODE, NAME, KOMITE, MKBD, NOTES)
            VALUES (TO_DATE(:recDate, 'DD-MM-YYYY'), :code, :name,
            :komite, :mkbd, :notes)
            """, nativeQuery = true)
    void insertHaircutHist(
            @Param("recDate") String recDate,
            @Param("code") String code,
            @Param("name") String name,
            @Param("komite") BigDecimal komite,
            @Param("mkbd") BigDecimal mkbd,
            @Param("notes") String notes
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM MKBD.HAIRCUT
            WHERE CODE = :code
            """, nativeQuery = true)
    void deleteHaircut(@Param("code") String code);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.HAIRCUT (REC_DATE, CODE, NAME, MKBD, NOTES)
            VALUES (TO_DATE(:date, 'YYYY-MM-DD'), :code, :name, :mkbd, :notes)
            """, nativeQuery = true)
    void insertHaircut(
            @Param("date") String date,
            @Param("code") String code,
            @Param("name") String name,
            @Param("mkbd") BigDecimal mkbd,
            @Param("notes") String notes
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.PARAM
            SET PARAMVAL = :date
            WHERE CODE = 'LAST_UPLOAD_HAIRCUT'
            """, nativeQuery = true)
    void updateLastUploadHaircut(@Param("date") String date);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.HAIRCUT
            SET NAME = :name,
                KOMITE = :komite,
                MKBD = :mkbd,
                NOTES = :notes
            WHERE CODE = :code
            """, nativeQuery = true)
    void updateHaircut(
            @Param("code") String code,
            @Param("name") String name,
            @Param("komite") BigDecimal komite,
            @Param("mkbd") BigDecimal mkbd,
            @Param("notes") String notes
    );

}