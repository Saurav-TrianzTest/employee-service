package com.example.employee.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class EmployeeResponseTest {

    @Test
    void gettersAndSetters_roundTrip() {
        EmployeeResponse r = new EmployeeResponse();
        LocalDateTime now = LocalDateTime.now();
        r.setId(1L);
        r.setFirstName("A");
        r.setLastName("B");
        r.setEmail("a@b.c");
        r.setDepartment("D");
        r.setJobTitle("J");
        r.setSalary(BigDecimal.TEN);
        r.setHireDate(LocalDate.of(2021, 2, 3));
        r.setCreatedAt(now);
        r.setUpdatedAt(now.plusDays(1));

        assertEquals(1L, r.getId());
        assertEquals("A", r.getFirstName());
        assertEquals("B", r.getLastName());
        assertEquals("a@b.c", r.getEmail());
        assertEquals("D", r.getDepartment());
        assertEquals("J", r.getJobTitle());
        assertEquals(BigDecimal.TEN, r.getSalary());
        assertEquals(LocalDate.of(2021, 2, 3), r.getHireDate());
        assertEquals(now, r.getCreatedAt());
        assertEquals(now.plusDays(1), r.getUpdatedAt());
    }

    @Test
    void newInstance_hasNullFields() {
        EmployeeResponse r = new EmployeeResponse();
        assertNull(r.getId());
        assertNull(r.getEmail());
        assertNull(r.getCreatedAt());
    }
}
