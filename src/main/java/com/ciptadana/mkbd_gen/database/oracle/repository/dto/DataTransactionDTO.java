package com.ciptadana.mkbd_gen.database.oracle.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTransactionDTO {

    private String externalRef;
    private Long seqNo;
    private String accountNo;
    private String currency;
    private String transDate;
    private String transType;
    private String transCode;
    private String debit;
    private String credit;
    private BigDecimal amount;
    private String openBal;
    private String closeBal;
    private String description;
    private String wsip;
    private String wstime;
    private String checked;
    private String invalidCol;
    private String invalidMsg;
    private String actTime;
    private String actResp;
}

