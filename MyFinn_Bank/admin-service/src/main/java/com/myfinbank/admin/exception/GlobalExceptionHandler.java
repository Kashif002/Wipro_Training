package com.myfinbank.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Global exception handler for all controllers
public class GlobalExceptionHandler {

    // Handle AdminNotFoundException
    @ExceptionHandler(AdminNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAdminNotFoundException(
            AdminNotFoundException ex, HttpServletRequest request) {
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Admin Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Handle CustomerNotFoundException
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleCustomerNotFoundException(
            CustomerNotFoundException ex, HttpServletRequest request) {
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Customer Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Handle LoanProcessingException
    @ExceptionHandler(LoanProcessingException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleLoanProcessingException(
            LoanProcessingException ex, HttpServletRequest request) {
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Loan Processing Error",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle UnauthorizedActionException
    @ExceptionHandler(UnauthorizedActionException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleUnauthorizedActionException(
            UnauthorizedActionException ex, HttpServletRequest request) {
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Unauthorized Action",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        
        // Check if it's an API request (starts with /api/)
        if (request.getRequestURI().startsWith("/api/")) {
            // Return JSON response for API requests
            modelAndView.setViewName("error/api-error");
        } else {
            // Return error page for web requests
            modelAndView.setViewName("error/general-error");
        }
        
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        
        return modelAndView;
    }

    // Helper method to create standardized error response
    private Map<String, Object> createErrorResponse(int status, String error, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", path);
        return errorResponse;
    }
}
