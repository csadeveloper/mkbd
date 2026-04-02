package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.TransaksiValas;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransaksiValasUpdateRequest {
    private String rowid;
    private String dealDate;
    private String typeTrx;
    private String currencyTrx;
    private BigDecimal trxVal;
    private BigDecimal unrealizePl;
    private BigDecimal rl;
}
