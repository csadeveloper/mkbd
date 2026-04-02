package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank;

import java.util.Date;

public interface MappingRekeningBankAutomatedResponse {
    String getCode();
    String getName();
    String getAccountType();
    String getSubaccountType();
    String getCurrency();
    String getBranch();
    String getCheckClientType();
    String getTrackVoucher();
    String getSubaccountShares();
    String getSuspended();
    String getClosuredBy();
    Date getClosuredDate();
    String getModifiedBy();
    Date getModifiedDate();
}
