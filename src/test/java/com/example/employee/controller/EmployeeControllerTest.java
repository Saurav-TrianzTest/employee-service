package com.example.employee.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/v1/employees");
        req.setServerName("localhost");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    private EmployeeResponse response(Long id) {
        EmployeeResponse r = new EmployeeResponse();
        r.setId(id);
        return r;
    }

    @Test
    void getAll_returnsServiceList() {
        List<EmployeeResponse> list = Arrays.asList(response(1L), response(2L));
        when(service.getAll()).thenReturn(list);
        assertEquals(2, controller.getAll().size());
    }

    @Test
    void getAll_empty_returnsEmptyList() {
        when(service.getAll()).thenReturn(Collections.emptyList());
        assertEquals(0, controller.getAll().size());
    }

    @Test
    void getById_returnsEmployee() {
        EmployeeResponse r = response(3L);
        when(service.getById(3L)).thenReturn(r);
        assertSame(r, controller.getById(3L));
    }

    @Test
    void getById_missing_propagatesException() {
        when(service.getById(9L)).thenThrow(new ResourceNotFoundException("nf"));
        assertThrows(ResourceNotFoundException.class, () -> controller.getById(9L));
    }

    @Test
    void create_returns201WithLocation() {
        EmployeeRequest req = new EmployeeRequest();
        EmployeeResponse created = response(10L);
        when(service.create(req)).thenReturn(created);

        ResponseEntity<EmployeeResponse> result = controller.create(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(created, result.getBody());
        assertEquals("/api/v1/employees/10", result.getHeaders().getLocation().getPath());
    }

    @Test
    void update_returnsUpdated() {
        EmployeeRequest req = new EmployeeRequest();
        EmployeeResponse updated = response(4L);
        when(service.update(4L, req)).thenReturn(updated);
        assertSame(updated, controller.update(4L, req));
    }

    @Test
    void delete_returns204() {
        ResponseEntity<Void> result = controller.delete(5L);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(service).delete(5L);
    }
}
