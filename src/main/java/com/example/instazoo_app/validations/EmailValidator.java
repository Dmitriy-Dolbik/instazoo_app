package com.example.instazoo_app.validations;

import com.example.instazoo_app.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$";
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email.matches(EMAIL_PATTERN);
    }
}
