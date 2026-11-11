package com.example.employee.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

class OracleJpaConfigTest {

    @Test
    void oracleHibernateCustomizer_returnsBean() {
        assertNotNull(new OracleJpaConfig().oracleHibernateCustomizer());
    }

    @Test
    void customize_setsOracleDialect() {
        HibernatePropertiesCustomizer customizer = new OracleJpaConfig().oracleHibernateCustomizer();
        Map<String, Object> props = new HashMap<>();
        customizer.customize(props);
        assertEquals("org.hibernate.dialect.OracleDialect", props.get("hibernate.dialect"));
    }

    @Test
    void customize_overridesExistingDialect() {
        HibernatePropertiesCustomizer customizer = new OracleJpaConfig().oracleHibernateCustomizer();
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", "other");
        customizer.customize(props);
        assertEquals("org.hibernate.dialect.OracleDialect", props.get("hibernate.dialect"));
    }
}
