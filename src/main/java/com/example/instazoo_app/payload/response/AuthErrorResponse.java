package com.example.instazoo_app.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthErrorResponse {
    private String message;
}
