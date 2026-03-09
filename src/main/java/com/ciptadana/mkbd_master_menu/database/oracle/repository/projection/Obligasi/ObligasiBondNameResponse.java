package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi;

import java.math.BigDecimal;
import java.util.Date;

public interface ObligasiBondNameResponse {
    String getCode();
    String getName();
    Integer getSuspended();
    Date getListingDate();
    String getCustody();
    String getScriptless();
    String getSharesType();
    Long getCountry();
    String getCurrency();
    Integer getUnitPerLot();
    String getRtCode();
    BigDecimal getMarkingPercent();
    BigDecimal getPriceCap();
    BigDecimal getExposurePercent();
    BigDecimal getExposureLimit();
    String getClosuredBy();
    Date getClosuredDate();
    String getModifiedBy();
    Date getModifiedDate();
    String getCountryName();
}
