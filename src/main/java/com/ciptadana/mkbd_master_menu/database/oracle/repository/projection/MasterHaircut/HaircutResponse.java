package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut;

import java.math.BigDecimal;
import java.util.Date;

public interface HaircutResponse {
    Date getRecDate();
    String getCode();
    String getName();
    BigDecimal getKomite();
    BigDecimal getMkbd();
    String getNotes();
}
