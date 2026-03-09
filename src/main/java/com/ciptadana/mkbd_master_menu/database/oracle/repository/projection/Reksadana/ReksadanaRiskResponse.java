package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana;

import java.math.BigDecimal;

public interface ReksadanaRiskResponse {
    String getType();
    BigDecimal getHaircut();
    BigDecimal getLimitpct();
}
