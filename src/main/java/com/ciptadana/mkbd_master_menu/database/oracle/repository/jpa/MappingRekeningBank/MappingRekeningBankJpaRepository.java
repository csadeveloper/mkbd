package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MappingRekeningBank;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.AutomatedAccountResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.F1Response;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankAutomatedResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MappingRekeningBankJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.MAP_BANK
            WHERE LENGTH(NCLIENT) = 8
            ORDER BY NOTES
            """, nativeQuery = true)
    List<MappingRekeningBankListResponse> findMappingRekeningBankList(
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.MAP_BANK
            WHERE LENGTH(NCLIENT) = 8
             AND NCLIENT = :nclient
            """, nativeQuery = true)
    List<MappingRekeningBankListResponse> findMappingRekeningBankListWithCondition(
            @Param("nclient") String nclient
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM DENPASAR.ACCOUNT
            WHERE CODE = :code
             AND CLOSURED_BY IS NULL
            """, nativeQuery = true)
    MappingRekeningBankAutomatedResponse findMappingRekeningBankAutomatedResponse(
            @Param("code") String code
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.MKBDACCOUNT
            WHERE CODE = :code
            """, nativeQuery = true)
    AutomatedAccountResponse findAutomatedAccountResponse(
            @Param("code") String code
    );

    @Transactional
    @Query(value = """
            SELECT ACCOUNT.CODE, ACCOUNT.NAME, CURRENCY.NAME AS CURRENCYNAME
            FROM DENPASAR.ACCOUNT,
             DENPASAR.CURRENCY
            WHERE ACCOUNT.ACCOUNT_TYPE = (SELECT ID FROM DENPASAR.ACCOUNT_TYPE WHERE
            NAME = 'BANK')
             AND ACCOUNT.CODE NOT IN (SELECT NCLIENT FROM MKBD.MAP_BANK WHERE
            LENGTH(NCLIENT) = 8)
             AND ACCOUNT.CURRENCY IS NOT NULL
             AND ACCOUNT.CURRENCY = CURRENCY.CODE(+)
             AND (
               ACCOUNT.CODE LIKE :prefix || '%'
               OR ACCOUNT.CODE IS NULL
             )
            ORDER BY ACCOUNT.CODE, ACCOUNT.NAME
            """, nativeQuery = true)
    List<F1Response> findF1Response(
            @Param("prefix") String prefix
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM MKBD.MAP_BANK
            WHERE NCLIENT = :nclient
            """, nativeQuery = true)
    void deleteMapBankByNclient(@Param("nclient") String nclient);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.MAP_BANK
            (NCLIENT, BANK_ACCOUNT_NO, NOTES, AFFILIATED, BANK_NAME, ISCLIENT,
            CURRENCY)
            VALUES (:nclient, :bankAccountNo, :notes,
            'N', :bankName, :isclient, :currency)
            """, nativeQuery = true)
    void insertMapBank(
            @Param("nclient") String nclient,
            @Param("bankAccountNo") String bankAccountNo,
            @Param("notes") String notes,
            @Param("bankName") String bankName,
            @Param("isclient") String isclient,
            @Param("currency") String currency
    );

}