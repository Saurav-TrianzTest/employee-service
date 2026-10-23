package com.example.employee.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.config.AppProperties;

@RestController
@RequestMapping("/api/v1")
public class AppInfoController {

    private final AppProperties appProperties;

    public AppInfoController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> info = new LinkedHashMap<String, String>();
        info.put("name", appProperties.getName());
        info.put("java", "11");
        info.put("database", "oracle");
        return info;
    }
}
