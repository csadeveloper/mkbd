package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.TransaksiValas;

import java.math.BigDecimal;
import java.util.Date;

public interface TransaksiValasListResponse {
    String getId();
    Date getRecdate();
    Date getDealDate();
    String getTypeTrx();
    String getCurrencyTrx();
    BigDecimal getTrxVal();
    BigDecimal getUnrealizePl();
    BigDecimal getRl();
}
