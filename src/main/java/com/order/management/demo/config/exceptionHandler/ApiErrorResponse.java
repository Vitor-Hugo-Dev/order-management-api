package com.order.management.demo.config.exceptionHandler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiErrorResponse {

    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
}