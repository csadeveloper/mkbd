package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MappingRekeningBank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapBankInsertRequest {
    private String nclient;
    private String bankAccountNo;
    private String notes;
    private String bankName;
    private String isclient;
    private String currency;
}
