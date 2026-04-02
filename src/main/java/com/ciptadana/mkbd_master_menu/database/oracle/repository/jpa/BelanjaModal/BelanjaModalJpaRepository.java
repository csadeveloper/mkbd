package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.BelanjaModal;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.BelanjaModal.BelanjaModalListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BelanjaModalJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_CAPITAL_EXPEND.*
            FROM MKBD.TRX_CAPITAL_EXPEND
            WHERE DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY DUE_DATE
            """, nativeQuery = true)
    List<BelanjaModalListResponse> findBelanjaModalList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_CAPITAL_EXPEND
            (RECDATE, COMMITMENT_DATE, DETAILS, DUE_DATE,
            REALIZE_VAL, UNREALIZE_VAL, RL)
            VALUES (SYSDATE, TO_DATE(:commitmentDate, 'YYYY-MM-DD'), :details,
            TO_DATE(:dueDate, 'YYYY-MM-DD'), :realizeVal, :unrealizeVal, :rl)
            """, nativeQuery = true)
    void insertBelanjaModal(
            @Param("commitmentDate") String commitmentDate,
            @Param("details") String details,
            @Param("dueDate") String dueDate,
            @Param("realizeVal") BigDecimal realizeVal,
            @Param("unrealizeVal") BigDecimal unrealizeVal,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_CAPITAL_EXPEND
            SET COMMITMENT_DATE = TO_DATE(:commitmentDate, 'YYYY-MM-DD'),
                DETAILS = :details,
                DUE_DATE = TO_DATE(:dueDate, 'YYYY-MM-DD'),
                REALIZE_VAL = :realizeVal,
                UNREALIZE_VAL = :unrealizeVal,
                RL = :rl
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateBelanjaModal(
            @Param("rowid") String rowid,
            @Param("commitmentDate") String commitmentDate,
            @Param("details") String details,
            @Param("dueDate") String dueDate,
            @Param("realizeVal") BigDecimal realizeVal,
            @Param("unrealizeVal") BigDecimal unrealizeVal,
            @Param("rl") BigDecimal rl
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_CAPITAL_EXPEND
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteBelanjaModal(@Param("rowid") String rowid);

}
