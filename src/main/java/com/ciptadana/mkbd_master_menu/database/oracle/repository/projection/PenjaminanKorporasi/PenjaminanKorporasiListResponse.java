package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanKorporasi;

import java.math.BigDecimal;
import java.util.Date;

public interface PenjaminanKorporasiListResponse {
    String getId();
    Date getRecdate();
    Date getContractDate();
    String getCounterParty();
    String getAffiliated();
    String getDetails();
    String getDays();
    Date getDueDate();
    BigDecimal getAssuranceVal();
    BigDecimal getRl();
}
