package com.clear.solutions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        System.out.println("Exception caught in GlobalExceptionHandler: " + ex.getMessage());

        if (ex.getMessage().contains("User not found")) {
            return new ResponseEntity<>("User not found. Please check the credentials and try again.",
                    HttpStatus.NOT_FOUND);
        }

        if (ex.getMessage().contains("User already exist")) {
            return new ResponseEntity<>("User already exist. Please add user only with unique email.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("From date must be before To date")) {
            return new ResponseEntity<>("From date must be before To date.",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        if (ex.getMessage().contains("User is null")) {
            return new ResponseEntity<>("User is null. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("Email is null or empty")) {
            return new ResponseEntity<>("Email is null or empty. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("Email format is invalid")) {
            return new ResponseEntity<>("Email format is invalid. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("First name or last name is null or empty")) {
            return new ResponseEntity<>("First name or last name is null or empty. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("User is less then")) {
            return new ResponseEntity<>("User is less then 18 years. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }

        if (ex.getMessage().contains("Birth date is invalid.")) {
            return new ResponseEntity<>("Birth date is invalid. Check input data please.",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                HttpStatus.BAD_REQUEST);
    }
}
