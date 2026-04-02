package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HaircutUpdateRequest {
    private String code;
    private String name;
    private BigDecimal komite;
    private BigDecimal mkbd;
    private String notes;
}
