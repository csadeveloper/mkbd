package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PengaturanParameter;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PengaturanParameter.ApplicationParameterResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PengaturanParameterJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.PARAM
            WHERE CODE IN ('DIRECTOR', 'KODEAB', 'DEPOSITO DI JAMIN LPS',
             'MARJIN_PCT_RL', 'MERGE_XLSDETAIL')
            """, nativeQuery = true)
    List<ApplicationParameterResponse> findApplicationParameter(
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.PARAM
            SET PARAMVAL = :value
            WHERE CODE = :code
            """, nativeQuery = true)
    void updateParameter(
            @Param("code") String code,
            @Param("value") String value
    );

}