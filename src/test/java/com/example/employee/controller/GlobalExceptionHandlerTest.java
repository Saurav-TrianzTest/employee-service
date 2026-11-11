package com.example.employee.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.employee.dto.ErrorResponse;
import com.example.employee.exception.DuplicateEmailException;
import com.example.employee.exception.ResourceNotFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/api/v1/employees/1");
    }

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<ErrorResponse> r = handler.handleNotFound(new ResourceNotFoundException("nf"), request);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
        assertEquals("nf", r.getBody().getMessage());
        assertEquals(404, r.getBody().getStatus());
        assertEquals("Not Found", r.getBody().getError());
        assertEquals("/api/v1/employees/1", r.getBody().getPath());
        assertNotNull(r.getBody().getTimestamp());
        assertNull(r.getBody().getFieldErrors());
    }

    @Test
    void handleDuplicate_returns409() {
        ResponseEntity<ErrorResponse> r = handler.handleDuplicate(new DuplicateEmailException("a@b.c"), request);
        assertEquals(HttpStatus.CONFLICT, r.getStatusCode());
        assertEquals("Employee with email already exists: a@b.c", r.getBody().getMessage());
    }

    @Test
    void handleUnreadable_returns400() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("bad", new MockHttpInputMessage(new byte[0]));
        ResponseEntity<ErrorResponse> r = handler.handleUnreadable(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        assertEquals("Request body is missing or malformed", r.getBody().getMessage());
    }

    @Test
    void handleValidation_returns400WithFieldErrors() throws Exception {
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "obj");
        binding.addError(new FieldError("obj", "email", "must not be blank"));
        binding.addError(new FieldError("obj", "salary", "must not be null"));
        java.lang.reflect.Method m = String.class.getMethod("length");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(m, -1), binding);

        ResponseEntity<ErrorResponse> r = handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        assertEquals("Request validation failed", r.getBody().getMessage());
        assertEquals(2, r.getBody().getFieldErrors().size());
        assertEquals("must not be blank", r.getBody().getFieldErrors().get("email"));
    }

    @Test
    void handleValidation_noFieldErrors_returnsEmptyMap() throws Exception {
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "obj");
        java.lang.reflect.Method m = String.class.getMethod("length");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(m, -1), binding);
        ResponseEntity<ErrorResponse> r = handler.handleValidation(ex, request);
        assertEquals(0, r.getBody().getFieldErrors().size());
    }

    @Test
    void handleUnexpected_returns500() {
        ResponseEntity<ErrorResponse> r = handler.handleUnexpected(new RuntimeException("boom"), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, r.getStatusCode());
        assertEquals("Unexpected server error", r.getBody().getMessage());
        assertEquals("Internal Server Error", r.getBody().getError());
    }
}
