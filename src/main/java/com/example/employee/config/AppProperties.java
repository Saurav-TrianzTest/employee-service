package com.example.employee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name = "employee-service";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
