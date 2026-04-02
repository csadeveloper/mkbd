package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsinCodeInsertRequest {
    private String type;
    private String shortcode;
    private String issuer;
    private String name;
    private String isincode;
    private String status;
}
