package com.example.employee.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.entity.Employee;

class EmployeeRepositoryTest {

    @Test
    void extendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(EmployeeRepository.class));
    }

    @Test
    void existsByEmailIgnoreCase_declaredWithBooleanReturn() throws NoSuchMethodException {
        assertEquals(boolean.class,
                EmployeeRepository.class.getMethod("existsByEmailIgnoreCase", String.class).getReturnType());
    }

    @Test
    void findByEmailIgnoreCase_declaredWithOptionalReturn() throws NoSuchMethodException {
        assertEquals(Optional.class,
                EmployeeRepository.class.getMethod("findByEmailIgnoreCase", String.class).getReturnType());
    }

    @Test
    void repository_managesEmployeeEntity() {
        assertEquals(Employee.class, Employee.class);
    }
}
