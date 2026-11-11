package com.example.employee.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

class WebConfigTest {

    @Test
    void addCorsMappings_registersApiMapping() {
        CorsRegistry registry = new CorsRegistry();
        new WebConfig().addCorsMappings(registry);

        Object configs = ReflectionTestUtils.invokeMethod(registry, "getCorsConfigurations");
        assertNotNull(configs);
        assertTrue(((java.util.Map<?, ?>) configs).containsKey("/api/**"));
    }

    @Test
    void addCorsMappings_configuresMethodsAndOrigins() {
        CorsRegistry registry = new CorsRegistry();
        new WebConfig().addCorsMappings(registry);

        @SuppressWarnings("unchecked")
        java.util.Map<String, org.springframework.web.cors.CorsConfiguration> configs =
                (java.util.Map<String, org.springframework.web.cors.CorsConfiguration>)
                        ReflectionTestUtils.invokeMethod(registry, "getCorsConfigurations");
        org.springframework.web.cors.CorsConfiguration cfg = configs.get("/api/**");
        assertTrue(cfg.getAllowedOrigins().contains("*"));
        assertTrue(cfg.getAllowedMethods().contains("DELETE"));
        assertTrue(cfg.getAllowedMethods().contains("PUT"));
        assertTrue(cfg.getAllowedHeaders().contains("*"));
    }
}
