package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.ReverseRepo;

import java.math.BigDecimal;
import java.util.Date;

public interface ReverseRepoListResponse {
    String getId();
    Date getRecdate();
    String getCounterParty();
    String getNshare();
    BigDecimal getQuantity();
    BigDecimal getPrice();
    BigDecimal getNominal();
    BigDecimal getReSellingValue();
    Date getInitialDate();
    Date getDueDate();
    BigDecimal getRatio();
    String getDays();
    String getType();
    String getNotes();
}
