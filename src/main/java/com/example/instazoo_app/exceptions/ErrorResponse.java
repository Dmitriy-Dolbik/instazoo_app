package com.example.instazoo_app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
}
