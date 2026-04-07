package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.ReverseRepo;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoCounterPartyResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.ReverseRepo.ReverseRepoListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReverseRepoJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_REVERSE_REPO.*
            FROM MKBD.TRX_REVERSE_REPO
            WHERE DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY NSHARE, INITIAL_DATE
            """, nativeQuery = true)
    List<ReverseRepoListResponse> findReverseRepoList(
            @Param("date") String date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_REVERSE_REPO
            (RECDATE, COUNTER_PARTY, NSHARE, QUANTITY, PRICE, NOMINAL,
            RE_SELLING_VALUE, INITIAL_DATE, DUE_DATE, RATIO, DAYS, TYPE, NOTES)
            VALUES (SYSDATE, :counterParty, :nshare, :quantity, :price, :nominal,
            :reSellingValue, TO_DATE(:initialDate, 'DD-MM-YYYY'),
            TO_DATE(:dueDate, 'DD-MM-YYYY'), :ratio, :days, :type, :notes)
            """, nativeQuery = true)
    void insertReverseRepo(
            @Param("counterParty") String counterParty,
            @Param("nshare") String nshare,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("reSellingValue") BigDecimal reSellingValue,
            @Param("initialDate") String initialDate,
            @Param("dueDate") String dueDate,
            @Param("ratio") BigDecimal ratio,
            @Param("days") BigDecimal days,
            @Param("type") String type,
            @Param("notes") String notes
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.TRX_REVERSE_REPO
            SET COUNTER_PARTY = :counterParty,
                NSHARE = :nshare,
                QUANTITY = :quantity,
                PRICE = :price,
                NOMINAL = :nominal,
                RE_SELLING_VALUE = :reSellingValue,
                INITIAL_DATE = TO_DATE(:initialDate, 'DD-MM-YYYY'),
                DUE_DATE = TO_DATE(:dueDate, 'DD-MM-YYYY'),
                RATIO = :ratio,
                DAYS = :days,
                TYPE = :type,
                NOTES = :notes
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateReverseRepo(
            @Param("rowid") String rowid,
            @Param("counterParty") String counterParty,
            @Param("nshare") String nshare,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("reSellingValue") BigDecimal reSellingValue,
            @Param("initialDate") String initialDate,
            @Param("dueDate") String dueDate,
            @Param("ratio") BigDecimal ratio,
            @Param("days") BigDecimal days,
            @Param("type") String type,
            @Param("notes") String notes
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MKBD.TRX_REVERSE_REPO
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteReverseRepo(@Param("rowid") String rowid);

}
