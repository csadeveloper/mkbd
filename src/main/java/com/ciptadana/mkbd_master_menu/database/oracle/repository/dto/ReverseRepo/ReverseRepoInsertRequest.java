package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReverseRepoInsertRequest {
    private String counterParty;
    private String nshare;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal nominal;
    private BigDecimal reSellingValue;
    private String initialDate;
    private String dueDate;
    private BigDecimal ratio;
    private BigDecimal days;
    private String type;
    private String notes;
}
