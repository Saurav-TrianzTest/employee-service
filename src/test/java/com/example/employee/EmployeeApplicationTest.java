package com.example.employee;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

class EmployeeApplicationTest {

    @Test
    void constructor_createsInstance() {
        assertNotNull(new EmployeeApplication());
    }

    @Test
    void class_isAnnotatedAsSpringBootApplication() {
        assertTrue(EmployeeApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }

    @Test
    void class_isAnnotatedWithConfigurationPropertiesScan() {
        assertTrue(EmployeeApplication.class.isAnnotationPresent(ConfigurationPropertiesScan.class));
    }

    @Test
    void mainMethod_exists() throws NoSuchMethodException {
        assertNotNull(EmployeeApplication.class.getMethod("main", String[].class));
    }
}
