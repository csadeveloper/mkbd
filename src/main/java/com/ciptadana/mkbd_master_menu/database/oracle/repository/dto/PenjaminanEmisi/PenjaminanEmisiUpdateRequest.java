package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanEmisi;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PenjaminanEmisiUpdateRequest {
    private String rowid;
    private String contractDate;
    private String type;
    private String counterParty;
    private String status;
    private BigDecimal portionValue;
    private BigDecimal haircutEfek;
    private BigDecimal absorbedVal;
    private BigDecimal creditGuarantee;
    private BigDecimal rl;
}
