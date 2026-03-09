package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo;

import java.math.BigDecimal;
import java.util.Date;

public interface RepoListResponse {
    String getId();
    Date getRecdate();
    String getCounterParty();
    String getNshare();
    BigDecimal getQuantity();
    BigDecimal getPrice();
    BigDecimal getNominal();
    BigDecimal getReBuyingValue();
    Date getInitialDate();
    Date getDueDate();
    BigDecimal getRatio();
    String getDays();
    String getType();
    String getNotes();
}
