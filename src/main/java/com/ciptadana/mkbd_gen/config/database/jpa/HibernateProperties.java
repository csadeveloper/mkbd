package com.ciptadana.mkbd_gen.config.database.jpa;

import lombok.Data;

@Data
public class HibernateProperties {

    private Boolean formatSql;
    private String ddlAuto;
    private int batchSize;
}
