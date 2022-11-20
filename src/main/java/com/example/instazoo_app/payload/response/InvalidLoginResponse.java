package com.example.instazoo_app.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String message;

    public InvalidLoginResponse() {
        this.message = "Incorrect credentials";
    }
}
