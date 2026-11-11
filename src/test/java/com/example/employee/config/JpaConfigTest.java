package com.example.employee.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

class JpaConfigTest {

    @Test
    void constructor_createsInstance() {
        assertNotNull(new JpaConfig());
    }

    @Test
    void class_hasConfigurationAndAuditingAnnotations() {
        assertTrue(JpaConfig.class.isAnnotationPresent(Configuration.class));
        assertTrue(JpaConfig.class.isAnnotationPresent(EnableJpaAuditing.class));
    }
}
