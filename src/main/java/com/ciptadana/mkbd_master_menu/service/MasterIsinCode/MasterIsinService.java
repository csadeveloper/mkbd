package com.ciptadana.mkbd_master_menu.service.MasterIsinCode;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.FileUploadResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeDeleteRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinDeleteRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterIsinCode.MasterIsinJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterIsinCode.IsinReksadanaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterIsinService {

    private final MasterIsinJpaRepository masterIsinJpaRepository;

    public List<IsinReksadanaResponse> getIsinReksadana(String type, String prefix) {
        if(type.equals("1")){
            return masterIsinJpaRepository.findIsinReksadana();
        }else if(type.equals("2")){
            return masterIsinJpaRepository.findIsinReksadanaByCondition(prefix);
        }
        return Collections.emptyList();
    }

    public void deleteIsinCode(String type, String isincode) {
        masterIsinJpaRepository.deleteIsinCode(type, isincode);
    }

    @Transactional
    public void deleteIsinCodeBulk(List<IsinCodeDeleteRequest> requests) {
        for (IsinCodeDeleteRequest request : requests) {
            masterIsinJpaRepository.deleteIsinCode(
                    request.getType(),
                    request.getIsincode()
            );
        }
    }

    @Transactional
    public void insertIsinCode(List<IsinCodeInsertRequest> requests) {
        for (IsinCodeInsertRequest request : requests) {
            masterIsinJpaRepository.insertIsinCode(
                    request.getType(),
                    request.getShortcode(),
                    request.getIssuer(),
                    request.getName(),
                    request.getIsincode(),
                    request.getStatus()
            );
        }
    }

    public void deleteXdmMasterIsin(String seccode, String isinCode) {
        masterIsinJpaRepository.deleteXdmMasterIsin(seccode, isinCode);
    }

    @Transactional
    public void deleteXdmMasterIsinBulk(List<XdmMasterIsinDeleteRequest> requests) {
        for (XdmMasterIsinDeleteRequest request : requests) {
            masterIsinJpaRepository.deleteXdmMasterIsin(
                    request.getSeccode(),
                    request.getIsinCode()
            );
        }
    }

    public void updateIsinCode(IsinCodeUpdateRequest request) {
        masterIsinJpaRepository.updateIsinCode(
                request.getType(),
                request.getShortcode(),
                request.getIssuer(),
                request.getName(),
                request.getIsincode(),
                request.getStatus()
        );
    }

    public void updateXdmMasterIsin(XdmMasterIsinUpdateRequest request) {
        masterIsinJpaRepository.updateXdmMasterIsin(
                request.getSeccode(),
                request.getSecname(),
                request.getSectype(),
                request.getIssuer(),
                request.getRegistrar(),
                request.getIsinCode(),
                request.getIsinStatus(),
                request.getListingDate(),
                request.getNoOfSec(),
                request.getStockExch(),
                request.getStatus(),
                request.getNominal(),
                request.getSecNum(),
                request.getExpDate(),
                request.getInterest(),
                request.getIntType(),
                request.getIntFreq(),
                request.getDaycount(),
                request.getCurr(),
                request.getSecForm(),
                request.getEffIsinDate(),
                request.getMatDate(),
                request.getSecSector()
        );
    }

    public void insertIsinCodeFromXdm(String seccode) {
        masterIsinJpaRepository.insertIsinCodeFromXdm(seccode);
    }

    @Transactional
    public void insertIsinCodeFromXdmBulk(List<String> seccodes) {
        for (String seccode : seccodes) {
            masterIsinJpaRepository.insertIsinCodeFromXdm(seccode);
        }
    }

    @Transactional
    public void insertXdmMasterIsin(List<XdmMasterIsinInsertRequest> requests) {
        for (XdmMasterIsinInsertRequest request : requests) {
            masterIsinJpaRepository.insertXdmMasterIsin(
                    request.getSeccode(),
                    request.getSecname(),
                    request.getSectype(),
                    request.getIssuer(),
                    request.getRegistrar(),
                    request.getIsinCode(),
                    request.getIsinStatus(),
                    request.getListingDate(),
                    request.getNoOfSec(),
                    request.getStockExch(),
                    request.getStatus(),
                    request.getNominal(),
                    request.getSecNum(),
                    request.getExpDate(),
                    request.getInterest(),
                    request.getIntType(),
                    request.getIntFreq(),
                    request.getDaycount(),
                    request.getCurr(),
                    request.getSecForm(),
                    request.getEffIsinDate(),
                    request.getMatDate(),
                    request.getSecSector()
            );
        }
    }

    // ==================== FILE UPLOAD OLD FORMAT ====================

    @Transactional
    public FileUploadResponse processOldFormatFile(MultipartFile file) {
        List<String> lines = readLines(file);
        String currentType = null;
        int totalLines = 0;
        int inserted = 0;
        int skipped = 0;

        List<String[]> parsedRows = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("#ISINCODE_")) {
                currentType = extractIsinType(line);
                continue;
            }
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }

            totalLines++;
            String[] cols = line.split("\\|", -1);

            if (cols.length < 6 || currentType == null || hasEmpty(cols[1], cols[2], cols[3], cols[4], cols[5])) {
                skipped++;
                log.warn("Skipped old format line (incomplete): {}", line);
                continue;
            }

            parsedRows.add(new String[]{currentType, cols[1], cols[2], cols[3], cols[4], cols[5]});
        }

        // Step 1: Delete existing data by type + isincode
        for (String[] row : parsedRows) {
            masterIsinJpaRepository.deleteIsinCode(row[0], row[4]);
        }

        // Step 2: Insert all
        for (String[] row : parsedRows) {
            masterIsinJpaRepository.insertIsinCode(row[0], row[1], row[2], row[3], row[4], row[5]);
            inserted++;
        }

        return new FileUploadResponse(totalLines, inserted, skipped);
    }

    // ==================== FILE UPLOAD NEW FORMAT ====================

    @Transactional
    public FileUploadResponse processNewFormatFile(MultipartFile file) {
        List<String> lines = readLines(file);
        int totalLines = 0;
        int inserted = 0;
        int skipped = 0;

        List<XdmParsedRow> parsedRows = new ArrayList<>();
        List<String> equitySeccodes = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("Securities Code")) {
                continue;
            }

            totalLines++;
            String[] cols = line.split("\\|", -1);

            if (cols.length < 3 || cols[0].isBlank() || cols[2].isBlank()) {
                skipped++;
                log.warn("Skipped new format line (incomplete): {}", line);
                continue;
            }

            XdmParsedRow row = parseNewFormatLine(cols);
            if (row == null) {
                skipped++;
                log.warn("Skipped new format line (invalid data): {}", line);
                continue;
            }

            parsedRows.add(row);
            if ("EQUITY".equals(cols[2])) {
                equitySeccodes.add(cols[0]);
            }
        }

        // Step 1: Delete existing data by seccode + isinCode
        for (XdmParsedRow row : parsedRows) {
            masterIsinJpaRepository.deleteXdmMasterIsin(row.seccode, row.isinCode);
        }

        // Step 2: Insert all to XDM_MASTER_ISIN
        for (XdmParsedRow row : parsedRows) {
            masterIsinJpaRepository.insertXdmMasterIsin(
                    row.seccode, row.secname, row.sectype, row.issuer, row.registrar,
                    row.isinCode, row.isinStatus, row.listingDate, row.noOfSec,
                    row.stockExch, row.status, row.nominal, row.secNum,
                    row.expDate, row.interest, row.intType, row.intFreq,
                    row.daycount, row.curr, row.secForm, row.effIsinDate,
                    row.matDate, row.secSector
            );
            inserted++;
        }

        // Step 3: Copy EQUITY records from XDM to ISINCODE
        for (String seccode : equitySeccodes) {
            masterIsinJpaRepository.insertIsinCodeFromXdm(seccode);
        }

        return new FileUploadResponse(totalLines, inserted, skipped);
    }

    // ==================== PRIVATE HELPERS ====================

    private List<String> readLines(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException("Gagal membaca file: " + e.getMessage(), e);
        }
    }

    private String extractIsinType(String headerLine) {
        // #ISINCODE_REKSADANA_20231001 → REKSADANA
        String withoutPrefix = headerLine.substring("#ISINCODE_".length());
        int lastUnderscore = withoutPrefix.lastIndexOf('_');
        if (lastUnderscore > 0) {
            return withoutPrefix.substring(0, lastUnderscore);
        }
        return withoutPrefix;
    }

    private boolean hasEmpty(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) {
                return true;
            }
        }
        return false;
    }

    private String formatDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        raw = raw.trim();
        if (raw.length() == 8) {
            return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
        }
        return raw;
    }

    private BigDecimal toBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new BigDecimal(value.trim());
    }

    private String safeGet(String[] cols, int index) {
        if (index >= cols.length) {
            return null;
        }
        String val = cols[index].trim();
        return val.isEmpty() ? null : val;
    }

    private XdmParsedRow parseNewFormatLine(String[] cols) {
        String sectype = cols[2].trim();
        return switch (sectype) {
            case "EQUITY" -> parseEquity(cols);
            case "CORPORATE BOND" -> parseCorporateBond(cols);
            case "WARRANT" -> parseWarrant(cols);
            case "RIGHT" -> parseRight(cols);
            default -> {
                log.warn("Unknown sectype: {}", sectype);
                yield null;
            }
        };
    }

    // EQUITY: SecCode|SecName|EQUITY|Issuer|Registrar|IsinCode|IsinStatus|ListingDate|NoOfSec|StockExch|Status|Nominal|Currency|SecForm|EffIsinDate|Sector
    private XdmParsedRow parseEquity(String[] cols) {
        if (cols.length < 16 || hasEmpty(cols[0], cols[1], cols[5])) return null;
        XdmParsedRow row = new XdmParsedRow();
        row.seccode = safeGet(cols, 0);
        row.secname = safeGet(cols, 1);
        row.sectype = safeGet(cols, 2);
        row.issuer = safeGet(cols, 3);
        row.registrar = safeGet(cols, 4);
        row.isinCode = safeGet(cols, 5);
        row.isinStatus = safeGet(cols, 6);
        row.listingDate = formatDate(safeGet(cols, 7));
        row.noOfSec = toBigDecimal(safeGet(cols, 8));
        row.stockExch = safeGet(cols, 9);
        row.status = safeGet(cols, 10);
        row.nominal = toBigDecimal(safeGet(cols, 11));
        row.curr = safeGet(cols, 12);
        row.secForm = safeGet(cols, 13);
        row.effIsinDate = formatDate(safeGet(cols, 14));
        row.secSector = safeGet(cols, 15);
        return row;
    }

    // CORPORATE BOND: SecCode|SecName|CORPORATE BOND|Issuer|IsinCode|ListingDate|StockExch|Status|Nominal|Interest|IntType|IntFreq|Daycount|Currency|SecForm|EffIsinDate|MatDate|Sector
    private XdmParsedRow parseCorporateBond(String[] cols) {
        if (cols.length < 18 || hasEmpty(cols[0], cols[1], cols[4])) return null;
        XdmParsedRow row = new XdmParsedRow();
        row.seccode = safeGet(cols, 0);
        row.secname = safeGet(cols, 1);
        row.sectype = safeGet(cols, 2);
        row.issuer = safeGet(cols, 3);
        row.isinCode = safeGet(cols, 4);
        row.listingDate = formatDate(safeGet(cols, 5));
        row.stockExch = safeGet(cols, 6);
        row.status = safeGet(cols, 7);
        row.nominal = toBigDecimal(safeGet(cols, 8));
        row.interest = toBigDecimal(safeGet(cols, 9));
        row.intType = safeGet(cols, 10);
        row.intFreq = safeGet(cols, 11);
        row.daycount = safeGet(cols, 12);
        row.curr = safeGet(cols, 13);
        row.secForm = safeGet(cols, 14);
        row.effIsinDate = formatDate(safeGet(cols, 15));
        row.matDate = formatDate(safeGet(cols, 16));
        row.secSector = safeGet(cols, 17);
        return row;
    }

    // WARRANT: SecCode|SecName|WARRANT|Issuer|Registrar|IsinCode|ListingDate|StockExch|Status|NoOfSec|Currency|SecForm|EffIsinDate|ExpDate|Sector
    private XdmParsedRow parseWarrant(String[] cols) {
        if (cols.length < 15 || hasEmpty(cols[0], cols[1], cols[5])) return null;
        XdmParsedRow row = new XdmParsedRow();
        row.seccode = safeGet(cols, 0);
        row.secname = safeGet(cols, 1);
        row.sectype = safeGet(cols, 2);
        row.issuer = safeGet(cols, 3);
        row.registrar = safeGet(cols, 4);
        row.isinCode = safeGet(cols, 5);
        row.listingDate = formatDate(safeGet(cols, 6));
        row.stockExch = safeGet(cols, 7);
        row.status = safeGet(cols, 8);
        row.noOfSec = toBigDecimal(safeGet(cols, 9));
        row.curr = safeGet(cols, 10);
        row.secForm = safeGet(cols, 11);
        row.effIsinDate = formatDate(safeGet(cols, 12));
        row.expDate = formatDate(safeGet(cols, 13));
        row.secSector = safeGet(cols, 14);
        return row;
    }

    // RIGHT: SecCode|SecName|RIGHT|Issuer|Registrar|IsinCode|ListingDate|StockExch|Status|NoOfSec|ExpDate|Interest|IntType|IntFreq|Daycount|Currency|SecForm|EffIsinDate|MatDate|Sector
    private XdmParsedRow parseRight(String[] cols) {
        if (cols.length < 20 || hasEmpty(cols[0], cols[1], cols[5])) return null;
        XdmParsedRow row = new XdmParsedRow();
        row.seccode = safeGet(cols, 0);
        row.secname = safeGet(cols, 1);
        row.sectype = safeGet(cols, 2);
        row.issuer = safeGet(cols, 3);
        row.registrar = safeGet(cols, 4);
        row.isinCode = safeGet(cols, 5);
        row.listingDate = formatDate(safeGet(cols, 6));
        row.stockExch = safeGet(cols, 7);
        row.status = safeGet(cols, 8);
        row.noOfSec = toBigDecimal(safeGet(cols, 9));
        row.expDate = formatDate(safeGet(cols, 10));
        row.interest = toBigDecimal(safeGet(cols, 11));
        row.intType = safeGet(cols, 12);
        row.intFreq = safeGet(cols, 13);
        row.daycount = safeGet(cols, 14);
        row.curr = safeGet(cols, 15);
        row.secForm = safeGet(cols, 16);
        row.effIsinDate = formatDate(safeGet(cols, 17));
        row.matDate = formatDate(safeGet(cols, 18));
        row.secSector = safeGet(cols, 19);
        return row;
    }

    private static class XdmParsedRow {
        String seccode;
        String secname;
        String sectype;
        String issuer;
        String registrar;
        String isinCode;
        String isinStatus;
        String listingDate;
        BigDecimal noOfSec;
        String stockExch;
        String status;
        BigDecimal nominal;
        String secNum;
        String expDate;
        BigDecimal interest;
        String intType;
        String intFreq;
        String daycount;
        String curr;
        String secForm;
        String effIsinDate;
        String matDate;
        String secSector;
    }

}
