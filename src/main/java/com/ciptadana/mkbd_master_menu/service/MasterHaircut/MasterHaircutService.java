package com.ciptadana.mkbd_master_menu.service.MasterHaircut;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutDeleteRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutHistDeleteRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutHistInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.FileUploadResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterHaircut.MasterHaircutJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterHaircutService {

    private final MasterHaircutJpaRepository masterHaircutJpaRepository;

    public List<HaircutResponse> getHaircutList() {
        return masterHaircutJpaRepository.findHaircutList();
    }

    public String getLastUpdate() {
        return masterHaircutJpaRepository.findLastUpdateDates();
    }

    public void deleteHaircutHist(String date) {
        masterHaircutJpaRepository.deleteHaircutHist(date);
    }

    @Transactional
    public void deleteHaircutHistBulk(List<HaircutHistDeleteRequest> requests) {
        for (HaircutHistDeleteRequest request : requests) {
            masterHaircutJpaRepository.deleteHaircutHist(request.getDate());
        }
    }

    @Transactional
    public void insertHaircutHist(List<HaircutHistInsertRequest> requests) {
        for (HaircutHistInsertRequest request : requests) {
            masterHaircutJpaRepository.insertHaircutHist(
                    request.getRecDate(),
                    request.getCode(),
                    request.getName(),
                    request.getKomite(),
                    request.getMkbd(),
                    request.getNotes()
            );
        }
    }

    public void deleteHaircut(String code) {
        masterHaircutJpaRepository.deleteHaircut(code);
    }

    @Transactional
    public void deleteHaircutBulk(List<HaircutDeleteRequest> requests) {
        for (HaircutDeleteRequest request : requests) {
            masterHaircutJpaRepository.deleteHaircut(request.getCode());
        }
    }

    @Transactional
    public void insertHaircut(List<HaircutInsertRequest> requests) {
        for (HaircutInsertRequest request : requests) {
            masterHaircutJpaRepository.insertHaircut(
                    request.getDate(),
                    request.getCode(),
                    request.getName(),
                    request.getMkbd(),
                    request.getNotes()
            );
        }
    }

    public void updateHaircut(HaircutUpdateRequest request) {
        masterHaircutJpaRepository.updateHaircut(
                request.getCode(),
                request.getName(),
                request.getKomite(),
                request.getMkbd(),
                request.getNotes()
        );
    }

    public void updateLastUploadHaircut(String date) {
        masterHaircutJpaRepository.updateLastUploadHaircut(date);
    }

    @Transactional
    public FileUploadResponse processHaircutFile(MultipartFile file) {
        List<String> lines = readLines(file);
        int totalLines = 0;
        int inserted = 0;
        int skipped = 0;

        String dateYmd = null;   // YYYY-MM-DD for delete/insertHaircut
        String dateDmy = null;   // DD-MM-YYYY for insertHaircutHist

        List<String[]> parsedRows = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("Tanggal")) {
                continue;
            }

            totalLines++;
            String[] cols = line.split("\\|", -1);

            if (cols.length < 5 || cols[0].isBlank() || cols[1].isBlank() || cols[2].isBlank() || cols[3].isBlank()) {
                skipped++;
                log.warn("Skipped haircut line (incomplete): {}", line);
                continue;
            }

            if (dateYmd == null) {
                String raw = cols[0].trim();
                dateYmd = raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
                dateDmy = raw.substring(6, 8) + "-" + raw.substring(4, 6) + "-" + raw.substring(0, 4);
            }

            parsedRows.add(cols);
        }

        if (dateYmd == null || parsedRows.isEmpty()) {
            return new FileUploadResponse(totalLines, 0, skipped);
        }

        // Step 1: Delete HAIRCUT_HIST by date (single call)
        masterHaircutJpaRepository.deleteHaircutHist(dateYmd);

        // Step 2: Insert all to HAIRCUT_HIST
        for (String[] cols : parsedRows) {
            masterHaircutJpaRepository.insertHaircutHist(
                    dateDmy,
                    cols[1].trim(),
                    cols[2].trim(),
                    null,
                    new BigDecimal(cols[3].trim()),
                    cols[4].trim()
            );
        }

        // Step 3: Delete HAIRCUT by code (bulk)
        for (String[] cols : parsedRows) {
            masterHaircutJpaRepository.deleteHaircut(cols[1].trim());
        }

        // Step 4: Insert all to HAIRCUT
        for (String[] cols : parsedRows) {
            masterHaircutJpaRepository.insertHaircut(
                    dateYmd,
                    cols[1].trim(),
                    cols[2].trim(),
                    new BigDecimal(cols[3].trim()),
                    cols[4].trim()
            );
            inserted++;
        }

        // Step 5: Update last upload date
        masterHaircutJpaRepository.updateLastUploadHaircut(dateYmd);

        return new FileUploadResponse(totalLines, inserted, skipped);
    }

    private List<String> readLines(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException("Gagal membaca file: " + e.getMessage(), e);
        }
    }

}
