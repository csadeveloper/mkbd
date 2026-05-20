package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ProsesMkbd;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProsesMkbdResponse {
    private final String trxDate;
    private final String periodDate;
    private final String procSuspendResult;
    private final String procUnsuspendResult;
    private final String procCustodyAvgOblCalcResult;
    private final long elapsedMs;
}
