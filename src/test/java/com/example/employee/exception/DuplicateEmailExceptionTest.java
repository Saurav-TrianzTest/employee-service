package com.example.employee.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DuplicateEmailExceptionTest {

    @Test
    void constructor_buildsMessageWithEmail() {
        assertEquals("Employee with email already exists: a@b.c", new DuplicateEmailException("a@b.c").getMessage());
    }

    @Test
    void constructor_nullEmail_includesNullText() {
        assertEquals("Employee with email already exists: null", new DuplicateEmailException(null).getMessage());
    }

    @Test
    void isRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DuplicateEmailException.class));
    }

    @Test
    void canBeThrown() {
        assertThrows(DuplicateEmailException.class, () -> {
            throw new DuplicateEmailException("x");
        });
    }
}
