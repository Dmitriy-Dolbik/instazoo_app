package com.example.instazoo_app.exceptions.handlers;

import com.example.instazoo_app.exceptions.AuthException;
import com.example.instazoo_app.exceptions.ErrorResponse;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleInvalidRequestValuesException(
            InvalidRequestValuesException exc) {
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException exc) {
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleAuthException(
            AuthException exc) {
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Создания ErrorResponse через builder (необходимо проставить @Builder над ErrorResponse
    /*@ExceptionHandler(InvalidRequestValuesException.class)
    public @ResponseBody
    ErrorResponse handleInvalidRequestValuesException(HttpServletResponse response,
                                                      Exception e){
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }*/
}
