package com.sample.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

/*
@author Ramandeep Singh
*/
@Configuration
@EnableJpaRepositories(basePackages = {
        "com.sample.repository"
})

public class DatabaseConfiguration {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public DatabaseConfiguration(JpaProperties jpaProperties,
                                 HibernateProperties hibernateProperties,
                                 List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.hibernatePropertiesCustomizers = hibernatePropertiesCustomizers;
    }

    //**************** BETA UPGRADEDB SLAVE CONFIGURATION **************/

    @Primary
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean primarySlaveEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primarySlaveEntityManagerFactory.getObject());
        return transactionManager;
    }

    //**************** BETA UPGRADEDB MASTER CONFIGURATION **************/
    @Bean(name = "primary_master_datasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariDataSource getMagentoMasterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    //    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryMasterEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("primary_master_datasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.sample")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>(
                this.hibernateProperties
                        .determineHibernateProperties(
                                jpaProperties.getProperties(),
                                new HibernateSettings().hibernatePropertiesCustomizers(hibernatePropertiesCustomizers)
                        ));
    }
}
