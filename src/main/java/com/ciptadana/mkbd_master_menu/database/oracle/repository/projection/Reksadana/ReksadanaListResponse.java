package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana;

import java.math.BigDecimal;
import java.util.Date;

public interface ReksadanaListResponse {
    String getId();
    Date getRecdate();
    Date getDealDate();
    String getType();
    String getNshareName();
    BigDecimal getAmountUp();
    BigDecimal getLastNab();
    BigDecimal getTotalNabMi();
    String getAffiliated();
    String getAccount();
    BigDecimal getHaircut();
    BigDecimal getLimitpct();
}
