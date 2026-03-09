package com.ciptadana.mkbd_master_menu.config.database;

import com.ciptadana.mkbd_master_menu.config.database.jpa.HibernateProperties;
import com.ciptadana.mkbd_master_menu.config.database.jpa.OracleJpaProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa",
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager")
public class OracleDBConfiguration {

    @Autowired
    private OracleJpaProperties jpaProperties;

    @Primary
    @Bean(name = "oraclePreDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.oracle.datasource")
    public DataSourceProperties preDataSourceProperties() {
        return new DataSourceProperties();
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        HibernateProperties hibernate = jpaProperties.getHibernateProperties();

        if (hibernate != null) {
            properties.put("hibernate.hbm2ddl.auto", hibernate.getDdlAuto());
            properties.put("hibernate.format_sql", hibernate.getFormatSql());
            properties.put("hibernate.jdbc.batch_size", hibernate.getBatchSize());
        }

        properties.put("hibernate.show_sql", jpaProperties.isShowSql());
        properties.put("hibernate.open-in-view", jpaProperties.isOpenInView());

        return properties;
    }


    @Primary
    @Bean(name = "oracleDataSource")
    @ConfigurationProperties("spring.oracle.datasource.hikari")
    public DataSource preDataSource() {
        return preDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("oracleDataSource") DataSource dataSource
    ) {

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = builder
                .dataSource(dataSource)
                .packages("com.ciptadana.mkbd_master_menu.database.oracle")
                .persistenceUnit("oracle")
                .build();

        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(hibernateProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}