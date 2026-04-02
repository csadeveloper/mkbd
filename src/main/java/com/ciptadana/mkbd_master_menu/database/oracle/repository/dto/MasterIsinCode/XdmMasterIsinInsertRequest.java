package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class XdmMasterIsinInsertRequest {
    private String seccode;
    private String secname;
    private String sectype;
    private String issuer;
    private String registrar;
    private String isinCode;
    private String isinStatus;
    private String listingDate;
    private BigDecimal noOfSec;
    private String stockExch;
    private String status;
    private BigDecimal nominal;
    private String secNum;
    private String expDate;
    private BigDecimal interest;
    private String intType;
    private String intFreq;
    private String daycount;
    private String curr;
    private String secForm;
    private String effIsinDate;
    private String matDate;
    private String secSector;
}
