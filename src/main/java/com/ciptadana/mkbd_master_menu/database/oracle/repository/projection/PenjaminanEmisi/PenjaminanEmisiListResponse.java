package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanEmisi;

import java.math.BigDecimal;
import java.util.Date;

public interface PenjaminanEmisiListResponse {
    String getId();
    Date getRecdate();
    Date getContractDate();
    String getType();
    String getCounterParty();
    String getStatus();
    BigDecimal getPortionValue();
    BigDecimal getHaircutEfek();
    BigDecimal getAbsorbedVal();
    BigDecimal getCreditGuarantee();
    BigDecimal getRl();
}
