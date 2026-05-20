package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData;

import java.math.BigDecimal;
import java.util.Date;

public interface Vd51Baris30DetailRow {
    Integer getTransFlag();
    String getTransModule();
    String getTransNo();
    Date getTransDate();
    Date getTransDuedate();
    String getAccount();
    String getSubaccount();
    String getDescription();
    BigDecimal getSignedAmount();
}
