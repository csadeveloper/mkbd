package com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterIsinCode;

import com.ciptadana.mkbd_master_menu.database.oracle.entity.NativeEntity;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterIsinCode.IsinReksadanaResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MasterIsinJpaRepository extends JpaRepository<NativeEntity, String> {

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.ISINCODE
            WHERE ISINTYPE = 'REKSADANA'
            ORDER BY ISINCODE
            """, nativeQuery = true)
    List<IsinReksadanaResponse> findIsinReksadana(
    );

    @Transactional
    @Query(value = """
            SELECT *
            FROM MKBD.ISINCODE
            WHERE ISINTYPE = 'REKSADANA'
             AND ISINCODE LIKE :prefix || '%'
            ORDER BY ISINCODE
            """, nativeQuery = true)
    List<IsinReksadanaResponse> findIsinReksadanaByCondition(
            @Param("prefix") String prefix
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM MKBD.ISINCODE
            WHERE ISINTYPE = :type
             AND ISINCODE = :isincode
            """, nativeQuery = true)
    void deleteIsinCode(
            @Param("type") String type,
            @Param("isincode") String isincode
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.ISINCODE
            (ISINTYPE, SHORTCODE, ISSUER, NAME, ISINCODE, STATUS)
            VALUES (:type, :shortcode, :issuer, :name, :isincode, :status)
            """, nativeQuery = true)
    void insertIsinCode(
            @Param("type") String type,
            @Param("shortcode") String shortcode,
            @Param("issuer") String issuer,
            @Param("name") String name,
            @Param("isincode") String isincode,
            @Param("status") String status
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM MKBD.XDM_MASTER_ISIN
            WHERE SECCODE = :seccode
             AND ISIN_CODE = :isinCode
            """, nativeQuery = true)
    void deleteXdmMasterIsin(
            @Param("seccode") String seccode,
            @Param("isinCode") String isinCode
    );

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MKBD.XDM_MASTER_ISIN
            (SECCODE, SECNAME, SECTYPE, ISSUER, REGISTRAR, ISIN_CODE, ISIN_STATUS,
            LISTING_DATE, NO_OF_SEC, STOCK_EXCH, STATUS, NOMINAL, SEC_NUM,
            EXP_DATE, INTEREST, INT_TYPE, INT_FREQ, DAYCOUNT, CURR,
            SEC_FORM, EFF_ISIN_DATE, MAT_DATE, SEC_SECTOR)
            VALUES (:seccode, :secname, :sectype, :issuer, :registrar, :isinCode, :isinStatus,
            TO_DATE(:listingDate, 'YYYY-MM-DD'), :noOfSec, :stockExch, :status, :nominal, :secNum,
            TO_DATE(:expDate, 'YYYY-MM-DD'), :interest, :intType, :intFreq, :daycount, :curr,
            :secForm, TO_DATE(:effIsinDate, 'YYYY-MM-DD'), TO_DATE(:matDate, 'YYYY-MM-DD'), :secSector)
            """, nativeQuery = true)
    void insertXdmMasterIsin(
            @Param("seccode") String seccode,
            @Param("secname") String secname,
            @Param("sectype") String sectype,
            @Param("issuer") String issuer,
            @Param("registrar") String registrar,
            @Param("isinCode") String isinCode,
            @Param("isinStatus") String isinStatus,
            @Param("listingDate") String listingDate,
            @Param("noOfSec") BigDecimal noOfSec,
            @Param("stockExch") String stockExch,
            @Param("status") String status,
            @Param("nominal") BigDecimal nominal,
            @Param("secNum") String secNum,
            @Param("expDate") String expDate,
            @Param("interest") BigDecimal interest,
            @Param("intType") String intType,
            @Param("intFreq") String intFreq,
            @Param("daycount") String daycount,
            @Param("curr") String curr,
            @Param("secForm") String secForm,
            @Param("effIsinDate") String effIsinDate,
            @Param("matDate") String matDate,
            @Param("secSector") String secSector
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.ISINCODE
            SET SHORTCODE = :shortcode,
                ISSUER = :issuer,
                NAME = :name,
                STATUS = :status
            WHERE ISINTYPE = :type
             AND ISINCODE = :isincode
            """, nativeQuery = true)
    void updateIsinCode(
            @Param("type") String type,
            @Param("shortcode") String shortcode,
            @Param("issuer") String issuer,
            @Param("name") String name,
            @Param("isincode") String isincode,
            @Param("status") String status
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE MKBD.XDM_MASTER_ISIN
            SET SECNAME = :secname,
                SECTYPE = :sectype,
                ISSUER = :issuer,
                REGISTRAR = :registrar,
                ISIN_STATUS = :isinStatus,
                LISTING_DATE = TO_DATE(:listingDate, 'YYYY-MM-DD'),
                NO_OF_SEC = :noOfSec,
                STOCK_EXCH = :stockExch,
                STATUS = :status,
                NOMINAL = :nominal,
                SEC_NUM = :secNum,
                EXP_DATE = TO_DATE(:expDate, 'YYYY-MM-DD'),
                INTEREST = :interest,
                INT_TYPE = :intType,
                INT_FREQ = :intFreq,
                DAYCOUNT = :daycount,
                CURR = :curr,
                SEC_FORM = :secForm,
                EFF_ISIN_DATE = TO_DATE(:effIsinDate, 'YYYY-MM-DD'),
                MAT_DATE = TO_DATE(:matDate, 'YYYY-MM-DD'),
                SEC_SECTOR = :secSector
            WHERE SECCODE = :seccode
             AND ISIN_CODE = :isinCode
            """, nativeQuery = true)
    void updateXdmMasterIsin(
            @Param("seccode") String seccode,
            @Param("secname") String secname,
            @Param("sectype") String sectype,
            @Param("issuer") String issuer,
            @Param("registrar") String registrar,
            @Param("isinCode") String isinCode,
            @Param("isinStatus") String isinStatus,
            @Param("listingDate") String listingDate,
            @Param("noOfSec") BigDecimal noOfSec,
            @Param("stockExch") String stockExch,
            @Param("status") String status,
            @Param("nominal") BigDecimal nominal,
            @Param("secNum") String secNum,
            @Param("expDate") String expDate,
            @Param("interest") BigDecimal interest,
            @Param("intType") String intType,
            @Param("intFreq") String intFreq,
            @Param("daycount") String daycount,
            @Param("curr") String curr,
            @Param("secForm") String secForm,
            @Param("effIsinDate") String effIsinDate,
            @Param("matDate") String matDate,
            @Param("secSector") String secSector
    );

}