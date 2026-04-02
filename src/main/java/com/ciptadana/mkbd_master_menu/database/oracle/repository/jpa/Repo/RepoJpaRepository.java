package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Repo;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoCounterPartyResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RepoJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT ROWID AS ID, MKBD.TRX_REPO.*
            FROM MKBD.TRX_REPO
            WHERE DUE_DATE >= TO_DATE(:date, 'YYYY-MM-DD')
            ORDER BY NSHARE, INITIAL_DATE
            """, nativeQuery = true)
    List<RepoListResponse> findRepoList(
            @Param("date") String date
    );

    @Transactional
    @Query(value = """
            SELECT COUNT(*)
            FROM
            	DENPASAR.CLIENT,
            	DENPASAR.CLIENT_TYPE,
            	DENPASAR.SALESMAN,
            	DENPASAR.CLIENT_CONTROL
            WHERE
            	CLIENT.CLIENT_TYPE = CLIENT_TYPE.ID
            	AND CLIENT.SALESMAN = SALESMAN.ID
            	AND CLIENT.CODE = CLIENT_CONTROL.CLIENT_ID
            	AND CLIENT_CONTROL.EFFECTIVE = ( SELECT MAX( EFFECTIVE ) FROM DENPASAR.CLIENT_CONTROL WHERE CLIENT_ID = CLIENT.CODE )
            	AND CLIENT.CLOSURED_BY IS NULL
            	AND (:name IS NULL OR CLIENT.NAME LIKE '%' || :name || '%')
            	AND (:code IS NULL OR CLIENT.CODE LIKE '%' || :code || '%')
            """, nativeQuery = true)
    long countRepoCounterParty(@Param("name") String name, @Param("code") String code);

    @Transactional
    @Query(value = """
            SELECT * FROM (
                SELECT a.*, ROWNUM rnum FROM (
                    SELECT
                        client.*,
                        client_type.name AS ClientTypeName,
                        salesman.name AS SalesmanName,
                        client_control.suspended
                    FROM
                        DENPASAR.CLIENT,
                        DENPASAR.CLIENT_TYPE,
                        DENPASAR.SALESMAN,
                        DENPASAR.CLIENT_CONTROL
                    WHERE
                        CLIENT.CLIENT_TYPE = CLIENT_TYPE.ID
                        AND CLIENT.SALESMAN = SALESMAN.ID
                        AND CLIENT.CODE = CLIENT_CONTROL.CLIENT_ID
                        AND CLIENT_CONTROL.EFFECTIVE = (
                            SELECT MAX( EFFECTIVE )
                            FROM DENPASAR.CLIENT_CONTROL
                            WHERE CLIENT_ID = CLIENT.CODE
                        )
                        AND CLIENT.CLOSURED_BY IS NULL
                        AND (:name IS NULL OR CLIENT.NAME LIKE '%' || :name || '%')
                        AND (:code IS NULL OR CLIENT.CODE LIKE '%' || :code || '%')
                    ORDER BY
                        CLIENT.NAME,
                        CLIENT.CODE
                ) a WHERE ROWNUM <= :endRow
            ) WHERE rnum > :startRow
            """, nativeQuery = true)
    List<RepoCounterPartyResponse> findRepoCounterParty(
            @Param("name") String name,
            @Param("code") String code,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    @Transactional
    @Query(value = """
            SELECT TO_DATE(:due_date, 'DD-MM-YYYY') - TO_DATE(:initial_date, 'DD-MM-YYYY') AS DAY
            FROM DUAL
            """, nativeQuery = true)
    BigDecimal findDays(
            @Param("due_date") String due_date,
            @Param("initial_date") String initial_date
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.TRX_REPO
            (RECDATE, COUNTER_PARTY, NSHARE, QUANTITY, PRICE, NOMINAL,
            RE_BUYING_VALUE, INITIAL_DATE, DUE_DATE, RATIO, DAYS, TYPE, NOTES)
            VALUES (SYSDATE, :counterParty, :nshare, :quantity, :price, :nominal,
            :reBuyingValue, TO_DATE(:initialDate, 'DD-MM-YYYY'),
            TO_DATE(:dueDate, 'DD-MM-YYYY'), :ratio, :days, :type, :notes)
            """, nativeQuery = true)
    void insertRepo(
            @Param("counterParty") String counterParty,
            @Param("nshare") String nshare,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("reBuyingValue") BigDecimal reBuyingValue,
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
            UPDATE MKBD.TRX_REPO
            SET COUNTER_PARTY = :counterParty,
                NSHARE = :nshare,
                QUANTITY = :quantity,
                PRICE = :price,
                NOMINAL = :nominal,
                RE_BUYING_VALUE = :reBuyingValue,
                INITIAL_DATE = TO_DATE(:initialDate, 'DD-MM-YYYY'),
                DUE_DATE = TO_DATE(:dueDate, 'DD-MM-YYYY'),
                RATIO = :ratio,
                DAYS = :days,
                TYPE = :type,
                NOTES = :notes
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void updateRepo(
            @Param("rowid") String rowid,
            @Param("counterParty") String counterParty,
            @Param("nshare") String nshare,
            @Param("quantity") BigDecimal quantity,
            @Param("price") BigDecimal price,
            @Param("nominal") BigDecimal nominal,
            @Param("reBuyingValue") BigDecimal reBuyingValue,
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
            DELETE FROM MKBD.TRX_REPO
            WHERE ROWID = :rowid
            """, nativeQuery = true)
    void deleteRepo(@Param("rowid") String rowid);

}
