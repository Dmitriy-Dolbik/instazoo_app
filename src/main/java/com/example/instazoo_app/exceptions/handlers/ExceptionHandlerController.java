package com.example.instazoo_app.exceptions.handlers;

import com.example.instazoo_app.exceptions.ErrorResponse;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleInvalidRequestValuesException(
            InvalidRequestValuesException exc){
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException exc){
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    // Другой способ создания ErrorResponse
    /*@ExceptionHandler(InvalidRequestValuesException.class)
    public @ResponseBody
    ErrorResponse handleInvalidRequestValuesException(HttpServletResponse response,
                                                      Exception e){
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ErrorResponse(e.getMessage());
    }*/
}
