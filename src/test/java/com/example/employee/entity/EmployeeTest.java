package com.example.employee.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void gettersAndSetters_roundTrip() {
        Employee e = new Employee();
        LocalDateTime now = LocalDateTime.now();
        e.setId(5L);
        e.setFirstName("A");
        e.setLastName("B");
        e.setEmail("a@b.c");
        e.setDepartment("D");
        e.setJobTitle("J");
        e.setSalary(new BigDecimal("1.00"));
        e.setHireDate(LocalDate.of(2020, 5, 5));
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        assertEquals(5L, e.getId());
        assertEquals("A", e.getFirstName());
        assertEquals("B", e.getLastName());
        assertEquals("a@b.c", e.getEmail());
        assertEquals("D", e.getDepartment());
        assertEquals("J", e.getJobTitle());
        assertEquals(new BigDecimal("1.00"), e.getSalary());
        assertEquals(LocalDate.of(2020, 5, 5), e.getHireDate());
        assertEquals(now, e.getCreatedAt());
        assertEquals(now, e.getUpdatedAt());
    }

    @Test
    void newInstance_idIsNull() {
        assertNull(new Employee().getId());
    }

    @Test
    void equals_sameInstance_true() {
        Employee e = new Employee();
        assertTrue(e.equals(e));
    }

    @Test
    void equals_sameId_true() {
        Employee a = new Employee();
        Employee b = new Employee();
        a.setId(1L);
        b.setId(1L);
        assertEquals(a, b);
    }

    @Test
    void equals_differentId_false() {
        Employee a = new Employee();
        Employee b = new Employee();
        a.setId(1L);
        b.setId(2L);
        assertNotEqualsHelper(a, b);
    }

    @Test
    void equals_nullIds_false() {
        assertNotEqualsHelper(new Employee(), new Employee());
    }

    @Test
    void equals_otherType_false() {
        Employee a = new Employee();
        a.setId(1L);
        assertFalse(a.equals("string"));
        assertFalse(a.equals(null));
    }

    @Test
    void hashCode_isConsistentAcrossInstances() {
        Employee a = new Employee();
        Employee b = new Employee();
        a.setId(1L);
        b.setId(2L);
        assertEquals(a.hashCode(), b.hashCode());
    }

    private void assertNotEqualsHelper(Employee a, Employee b) {
        assertFalse(a.equals(b));
    }
}
