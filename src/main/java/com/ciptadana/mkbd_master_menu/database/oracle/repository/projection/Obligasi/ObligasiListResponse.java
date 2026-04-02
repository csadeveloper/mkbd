package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi;

import java.math.BigDecimal;
import java.util.Date;

public interface ObligasiListResponse {
    String getId();
    Date getRecdate();
    Date getDealDate();
    String getNshareName();
    String getRating();
    BigDecimal getQuantity();
    BigDecimal getPrice();
    BigDecimal getMarketValue();
    BigDecimal getHaircutValue();
    BigDecimal getHaircutAfter();
    BigDecimal getConcernRisk();
    String getAffiliated();
    String getGroupShare();
    BigDecimal getAcquisitionPrice();
    String getSukuk();
    BigDecimal getHaircut();


}
