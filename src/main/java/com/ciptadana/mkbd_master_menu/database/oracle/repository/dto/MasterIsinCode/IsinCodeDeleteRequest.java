package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsinCodeDeleteRequest {
    private String type;
    private String isincode;
}
