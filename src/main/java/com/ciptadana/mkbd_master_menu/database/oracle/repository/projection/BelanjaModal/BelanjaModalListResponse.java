package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.BelanjaModal;

import java.math.BigDecimal;
import java.util.Date;

public interface BelanjaModalListResponse {
    String getId();
    Date getRecdate();
    Date getCommitmentDate();
    String getDetails();
    Date getDueDate();
    BigDecimal getRealizeVal();
    BigDecimal getUnrealizeVal();
    BigDecimal getRl();
}
