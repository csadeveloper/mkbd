package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn;

import java.math.BigDecimal;
import java.util.Date;

public interface SbnListResponse {
    Date getRecdate();
    String getNshare();
    Date getDueDate();
    BigDecimal getPrice();
    BigDecimal getNominal();
    BigDecimal getMarketValue();
    Short getAffiliated();
    String getGroupShare();
    BigDecimal getAcquisitionPrice();
}
