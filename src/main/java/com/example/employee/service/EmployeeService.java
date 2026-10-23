package com.example.employee.service;

import java.util.List;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse update(Long id, EmployeeRequest request);

    EmployeeResponse getById(Long id);

    List<EmployeeResponse> getAll();

    void delete(Long id);
}
