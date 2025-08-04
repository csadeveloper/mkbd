package com.ciptadana.mkbd_gen.config.database.jpa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.oracle.jpa")
public class OracleJpaProperties {
    private HibernateProperties hibernateProperties;
    private boolean showSql;
    private boolean openInView;
}
