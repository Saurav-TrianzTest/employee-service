package com.example.employee.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OracleJpaConfig {

    @Bean
    public HibernatePropertiesCustomizer oracleHibernateCustomizer() {
        return new HibernatePropertiesCustomizer() {
            @Override
            public void customize(Map<String, Object> hibernateProperties) {
                hibernateProperties.put(
                        "hibernate.dialect",
                        "org.hibernate.dialect.Oracle12cDialect");
            }
        };
    }
}
