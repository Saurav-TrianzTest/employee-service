package com.example.employee.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppPropertiesTest {

    @Test
    void defaultName_isEmployeeService() {
        assertEquals("employee-service", new AppProperties().getName());
    }

    @Test
    void setName_updatesName() {
        AppProperties p = new AppProperties();
        p.setName("other");
        assertEquals("other", p.getName());
    }
}
