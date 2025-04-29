package com.storysoksok.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ValidErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private final Map<String, String> validation;

    public void addValidation(String field, String message) {
        this.validation.put(field, message);
    }
}