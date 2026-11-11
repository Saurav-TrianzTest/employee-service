package com.example.employee.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class EmployeeRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        factory.close();
    }

    private EmployeeRequest valid() {
        EmployeeRequest r = new EmployeeRequest();
        r.setFirstName("Ada");
        r.setLastName("Lovelace");
        r.setEmail("ada@example.com");
        r.setDepartment("Eng");
        r.setJobTitle("Dev");
        r.setSalary(new BigDecimal("100.50"));
        r.setHireDate(LocalDate.of(2020, 1, 1));
        return r;
    }

    @Test
    void gettersAndSetters_roundTrip() {
        EmployeeRequest r = valid();
        assertEquals("Ada", r.getFirstName());
        assertEquals("Lovelace", r.getLastName());
        assertEquals("ada@example.com", r.getEmail());
        assertEquals("Eng", r.getDepartment());
        assertEquals("Dev", r.getJobTitle());
        assertEquals(new BigDecimal("100.50"), r.getSalary());
        assertEquals(LocalDate.of(2020, 1, 1), r.getHireDate());
    }

    @Test
    void newInstance_hasNullFields() {
        EmployeeRequest r = new EmployeeRequest();
        assertNull(r.getFirstName());
        assertNull(r.getSalary());
        assertNull(r.getHireDate());
    }

    @Test
    void validate_validRequest_noViolations() {
        assertTrue(validator.validate(valid()).isEmpty());
    }

    @Test
    void validate_emptyRequest_hasViolations() {
        Set<ConstraintViolation<EmployeeRequest>> v = validator.validate(new EmployeeRequest());
        assertEquals(7, v.size());
    }

    @Test
    void validate_invalidEmail_hasViolation() {
        EmployeeRequest r = valid();
        r.setEmail("not-an-email");
        assertEquals(1, validator.validate(r).size());
    }

    @Test
    void validate_negativeSalary_hasViolation() {
        EmployeeRequest r = valid();
        r.setSalary(new BigDecimal("-1"));
        assertEquals(1, validator.validate(r).size());
    }

    @Test
    void validate_futureHireDate_hasViolation() {
        EmployeeRequest r = valid();
        r.setHireDate(LocalDate.now().plusDays(5));
        assertEquals(1, validator.validate(r).size());
    }

    @Test
    void validate_tooLongFirstName_hasViolation() {
        EmployeeRequest r = valid();
        r.setFirstName("x".repeat(51));
        assertEquals(1, validator.validate(r).size());
    }

    @Test
    void validate_salaryTooManyFractionDigits_hasViolation() {
        EmployeeRequest r = valid();
        r.setSalary(new BigDecimal("1.123"));
        assertEquals(1, validator.validate(r).size());
    }
}
