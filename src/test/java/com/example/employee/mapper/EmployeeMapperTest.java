package com.example.employee.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.entity.Employee;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EmployeeMapper();
    }

    private EmployeeRequest request() {
        EmployeeRequest r = new EmployeeRequest();
        r.setFirstName("  Ada ");
        r.setLastName(" Lovelace  ");
        r.setEmail("  ADA@Example.COM ");
        r.setDepartment(" Eng ");
        r.setJobTitle(" Dev ");
        r.setSalary(new BigDecimal("123.45"));
        r.setHireDate(LocalDate.of(2020, 1, 15));
        return r;
    }

    @Test
    void toEntity_trimsAndNormalizes() {
        Employee e = mapper.toEntity(request());
        assertEquals("Ada", e.getFirstName());
        assertEquals("Lovelace", e.getLastName());
        assertEquals("ada@example.com", e.getEmail());
        assertEquals("Eng", e.getDepartment());
        assertEquals("Dev", e.getJobTitle());
        assertEquals(new BigDecimal("123.45"), e.getSalary());
        assertEquals(LocalDate.of(2020, 1, 15), e.getHireDate());
    }

    @Test
    void update_overwritesExistingEntity() {
        Employee e = new Employee();
        e.setId(8L);
        e.setFirstName("Old");
        mapper.update(e, request());
        assertEquals(8L, e.getId());
        assertEquals("Ada", e.getFirstName());
        assertEquals("ada@example.com", e.getEmail());
    }

    @Test
    void toResponse_copiesAllFields() {
        Employee e = new Employee();
        LocalDateTime now = LocalDateTime.now();
        e.setId(1L);
        e.setFirstName("A");
        e.setLastName("B");
        e.setEmail("a@b.c");
        e.setDepartment("D");
        e.setJobTitle("J");
        e.setSalary(BigDecimal.ONE);
        e.setHireDate(LocalDate.of(2019, 9, 9));
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        EmployeeResponse r = mapper.toResponse(e);

        assertEquals(1L, r.getId());
        assertEquals("A", r.getFirstName());
        assertEquals("B", r.getLastName());
        assertEquals("a@b.c", r.getEmail());
        assertEquals("D", r.getDepartment());
        assertEquals("J", r.getJobTitle());
        assertEquals(BigDecimal.ONE, r.getSalary());
        assertEquals(LocalDate.of(2019, 9, 9), r.getHireDate());
        assertEquals(now, r.getCreatedAt());
        assertEquals(now, r.getUpdatedAt());
    }

    @Test
    void normalizeEmail_trimsAndLowercases() {
        assertEquals("a@b.c", mapper.normalizeEmail("  A@B.C  "));
    }

    @Test
    void normalizeEmail_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> mapper.normalizeEmail(null));
    }

    @Test
    void toEntity_nullFirstName_throwsNpe() {
        EmployeeRequest r = request();
        r.setFirstName(null);
        assertThrows(NullPointerException.class, () -> mapper.toEntity(r));
    }
}
