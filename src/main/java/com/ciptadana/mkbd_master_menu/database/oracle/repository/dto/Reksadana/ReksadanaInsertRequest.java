package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Reksadana;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReksadanaInsertRequest {
    private String dealDate;
    private String type;
    private String nshareName;
    private BigDecimal amountUp;
    private BigDecimal lastNab;
    private BigDecimal totalNabMi;
    private String affiliated;
}
