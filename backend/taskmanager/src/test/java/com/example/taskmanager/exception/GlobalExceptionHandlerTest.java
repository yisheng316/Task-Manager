package com.example.taskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void handleEntityNotFoundException_ShouldReturnNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<Object> response = exceptionHandler.handleEntityNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Entity not found", body.get("message"));
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<Object> response = exceptionHandler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("An unexpected error occurred", body.get("message"));
        assertEquals("Unexpected error", body.get("details"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}