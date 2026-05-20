package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData;

import java.math.BigDecimal;
import java.util.Date;

public interface CoaDetailRow {
    String getVdSekuritasCat();
    String getVdLineNo();
    String getAccount();
    String getAccountName();
    String getNshare();
    String getSubaccount();
    Integer getLedgerSubaccountType();
    Integer getAcctSubaccountType();
    String getBranch();
    String getNfaktur();
    String getDescription();
    Integer getCurrency();
    Date getTransDate();
    Integer getTransCurrent();
    BigDecimal getAmountidr();
    BigDecimal getAbsAmountidr();
    BigDecimal getAmountContrib();
}
