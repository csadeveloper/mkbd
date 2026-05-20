package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class MkbdAllDataResponse implements Serializable {
    private String genDate;
    private MkbdDetailData detail;
    private MkbdSummaryData summary;
}
