package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanKorporasi;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PenjaminanKorporasiInsertRequest {
    private String contractDate;
    private String counterParty;
    private String affiliated;
    private String details;
    private Integer days;
    private String dueDate;
    private BigDecimal assuranceVal;
    private BigDecimal rl;
}
