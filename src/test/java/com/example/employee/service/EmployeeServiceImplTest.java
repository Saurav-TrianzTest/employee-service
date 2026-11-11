package com.example.employee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateEmailException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository, new EmployeeMapper());
    }

    @Test
    void createStoresNormalizedEmployee() {
        EmployeeRequest request = request("Ada", "Lovelace", "Ada@Example.com");
        when(employeeRepository.existsByEmailIgnoreCase("ada@example.com")).thenReturn(false);
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(1L);
            return employee;
        });

        EmployeeResponse response = employeeService.create(request);

        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals("ada@example.com", response.getEmail());
        assertEquals("Ada", response.getFirstName());
        assertEquals("Engineering", response.getDepartment());
    }

    @Test
    void createRejectsDuplicateEmail() {
        EmployeeRequest request = request("Ada", "Lovelace", "ada@example.com");
        when(employeeRepository.existsByEmailIgnoreCase("ada@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> employeeService.create(request));
        verify(employeeRepository, never()).saveAndFlush(any(Employee.class));
    }

    @Test
    void getByIdThrowsWhenMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(99L));
    }

    @Test
    void updateChangesExistingEmployee() {
        Employee existing = new Employee();
        existing.setId(4L);
        existing.setEmail("old@example.com");
        EmployeeRequest request = request("Grace", "Hopper", "grace@example.com");
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(employeeRepository.findByEmailIgnoreCase("grace@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.saveAndFlush(existing)).thenReturn(existing);

        EmployeeResponse response = employeeService.update(4L, request);

        assertEquals("Grace", response.getFirstName());
        assertEquals("grace@example.com", response.getEmail());
    }

    @Test
    void deleteRemovesExistingEmployee() {
        Employee existing = new Employee();
        existing.setId(7L);
        when(employeeRepository.findById(7L)).thenReturn(Optional.of(existing));

        employeeService.delete(7L);

        verify(employeeRepository).delete(existing);
    }

    @Test
    void updateRejectsEmailOwnedByAnotherEmployee() {
        Employee existing = new Employee();
        existing.setId(4L);
        Employee other = new Employee();
        other.setId(5L);
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(employeeRepository.findByEmailIgnoreCase("grace@example.com")).thenReturn(Optional.of(other));

        assertThrows(DuplicateEmailException.class,
                () -> employeeService.update(4L, request("Grace", "Hopper", "grace@example.com")));
        verify(employeeRepository, never()).saveAndFlush(any(Employee.class));
    }

    @Test
    void updateAllowsSameEmployeeKeepingEmail() {
        Employee existing = new Employee();
        existing.setId(4L);
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(employeeRepository.findByEmailIgnoreCase("grace@example.com")).thenReturn(Optional.of(existing));
        when(employeeRepository.saveAndFlush(existing)).thenReturn(existing);

        EmployeeResponse response = employeeService.update(4L, request("Grace", "Hopper", "grace@example.com"));

        assertEquals("Hopper", response.getLastName());
    }

    @Test
    void updateThrowsWhenEmployeeMissing() {
        when(employeeRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.update(404L, request("A", "B", "a@b.c")));
    }

    @Test
    void getByIdReturnsMappedEmployee() {
        Employee existing = new Employee();
        existing.setId(2L);
        existing.setFirstName("Linus");
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(existing));

        EmployeeResponse response = employeeService.getById(2L);

        assertEquals(Long.valueOf(2L), response.getId());
        assertEquals("Linus", response.getFirstName());
    }

    @Test
    void getAllMapsAllEmployees() {
        Employee a = new Employee();
        a.setId(1L);
        Employee b = new Employee();
        b.setId(2L);
        when(employeeRepository.findAll()).thenReturn(java.util.Arrays.asList(a, b));

        java.util.List<EmployeeResponse> result = employeeService.getAll();

        assertEquals(2, result.size());
        assertEquals(Long.valueOf(2L), result.get(1).getId());
    }

    @Test
    void getAllReturnsEmptyListWhenNoEmployees() {
        when(employeeRepository.findAll()).thenReturn(java.util.Collections.<Employee>emptyList());

        assertEquals(0, employeeService.getAll().size());
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(1L));
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    private EmployeeRequest request(String firstName, String lastName, String email) {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setDepartment("Engineering");
        request.setJobTitle("Developer");
        request.setSalary(new BigDecimal("90000.00"));
        request.setHireDate(LocalDate.of(2020, 1, 15));
        return request;
    }
}
