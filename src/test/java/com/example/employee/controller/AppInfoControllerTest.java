package com.example.employee.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.employee.config.AppProperties;

class AppInfoControllerTest {

    @Test
    void info_returnsDefaultProperties() {
        Map<String, String> info = new AppInfoController(new AppProperties()).info();
        assertEquals("employee-service", info.get("name"));
        assertEquals("17", info.get("java"));
        assertEquals("oracle", info.get("database"));
        assertEquals(3, info.size());
    }

    @Test
    void info_usesConfiguredName() {
        AppProperties p = new AppProperties();
        p.setName("custom");
        assertEquals("custom", new AppInfoController(p).info().get("name"));
    }

    @Test
    void info_preservesKeyOrder() {
        Map<String, String> info = new AppInfoController(new AppProperties()).info();
        assertEquals(java.util.List.of("name", "java", "database"), new ArrayList<>(info.keySet()));
    }
}
