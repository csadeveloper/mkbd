package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging;

import java.math.BigDecimal;
import java.util.Date;

public interface HedgingListResponse {
    String getId();
    Date getRecdate();
    Date getDueDate();
    String getNshare();
    BigDecimal getNominal();
    BigDecimal getNominalHedging();
    BigDecimal getHedgingVal();
    BigDecimal getHcHedgingVal();
    BigDecimal getHcVal();
    String getName();

}
