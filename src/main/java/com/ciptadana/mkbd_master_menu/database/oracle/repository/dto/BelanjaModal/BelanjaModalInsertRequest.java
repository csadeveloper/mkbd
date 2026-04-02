package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.BelanjaModal;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BelanjaModalInsertRequest {
    private String commitmentDate;
    private String details;
    private String dueDate;
    private BigDecimal realizeVal;
    private BigDecimal unrealizeVal;
    private BigDecimal rl;
}
