package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging;

import java.math.BigDecimal;
import java.util.Date;

public interface HedgingEfekResponse {
    Date getRecDate();
    String getCode();
    String getName();
    String getKomite();
    BigDecimal getMkbd();
    String getNotes();
}
