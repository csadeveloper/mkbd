package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class SbnCalculationResponse {
    private final String id;
    private final Date recdate;
    private final String nshare;
    private final Date dueDate;
    private final BigDecimal price;
    private final BigDecimal nominal;
    private final BigDecimal marketValue;
    private final String affiliated;
    private final String groupShare;
    private final BigDecimal acquisitionPrice;

    private final BigDecimal sisaMasaJatuhTempo;
    private final String persentaseHaircut;
    private final BigDecimal nilaiHaircut;
    private final BigDecimal nilaiSetelahHaircut;
    private final BigDecimal risikoKonsentrasi;
}
