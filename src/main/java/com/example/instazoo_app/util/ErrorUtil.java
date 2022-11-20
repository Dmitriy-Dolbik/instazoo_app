package com.example.instazoo_app.util;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ErrorUtil {
    public static String createErrorMessageToClient(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();

        //Указывает на аннотацию и ошибку, которая к ней  относится
        List<ObjectError> objErrors = bindingResult.getAllErrors();
        if (!CollectionUtils.isEmpty(objErrors)){
            for (ObjectError error : objErrors){
                errorMsg.append(error.getCode())
                        .append(" - ")
                        .append(error.getDefaultMessage() == null ? error.getCode():error.getDefaultMessage())
                        .append("; ");
            }
        }
        return errorMsg.toString();
    }
}
