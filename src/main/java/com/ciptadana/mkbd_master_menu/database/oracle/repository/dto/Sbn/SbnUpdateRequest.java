package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SbnUpdateRequest {
    private String rowid;
    private String nshare;
    private String dueDate;
    private BigDecimal price;
    private BigDecimal nominal;
    private BigDecimal marketValue;
    private BigDecimal acquisitionPrice;
    private Short affiliated;
}
