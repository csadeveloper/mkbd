package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileUploadResponse {
    private int totalLines;
    private int inserted;
    private int skipped;
}
