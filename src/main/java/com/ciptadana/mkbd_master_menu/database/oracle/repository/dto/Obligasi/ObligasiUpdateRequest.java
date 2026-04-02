package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Obligasi;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ObligasiUpdateRequest {
    private String rowid;
    private String dealDate;
    private String nshareName;
    private String rating;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal marketValue;
    private BigDecimal haircutValue;
    private BigDecimal haircutAfter;
    private BigDecimal concernRisk;
    private BigDecimal acquisitionPrice;
    private String affiliated;
    private String sukuk;
}
