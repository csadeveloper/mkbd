package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Hedging;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HedgingUpdateRequest {
    private String rowid;
    private String dueDate;
    private String nshare;
    private BigDecimal nominal;
    private BigDecimal nominalHedging;
    private BigDecimal hedgingVal;
    private BigDecimal hcHedgingVal;
    private BigDecimal hcVal;
}
