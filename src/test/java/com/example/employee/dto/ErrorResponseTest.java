package com.example.employee.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class ErrorResponseTest {

    @Test
    void gettersAndSetters_roundTrip() {
        ErrorResponse e = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        e.setTimestamp(now);
        e.setStatus(404);
        e.setError("Not Found");
        e.setMessage("msg");
        e.setPath("/p");
        e.setFieldErrors(Map.of("a", "b"));

        assertEquals(now, e.getTimestamp());
        assertEquals(404, e.getStatus());
        assertEquals("Not Found", e.getError());
        assertEquals("msg", e.getMessage());
        assertEquals("/p", e.getPath());
        assertEquals("b", e.getFieldErrors().get("a"));
    }

    @Test
    void newInstance_defaults() {
        ErrorResponse e = new ErrorResponse();
        assertEquals(0, e.getStatus());
        assertNull(e.getFieldErrors());
    }

    @Test
    void serialization_omitsNullFields() throws Exception {
        ErrorResponse e = new ErrorResponse();
        e.setStatus(400);
        e.setMessage("bad");
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(e);
        assertTrue(json.contains("\"status\":400"));
        assertFalse(json.contains("fieldErrors"));
        assertFalse(json.contains("timestamp"));
    }
}
