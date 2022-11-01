package com.example.instazoo_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserWasNotUpdatedException extends RuntimeException{
    public UserWasNotUpdatedException(String msg){
        super(msg);
    }
}
