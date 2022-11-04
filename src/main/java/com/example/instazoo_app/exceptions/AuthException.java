package com.example.instazoo_app.exceptions;

public class AuthException extends RuntimeException {
    public AuthException (String msg) {
        super(msg);
    }
}
