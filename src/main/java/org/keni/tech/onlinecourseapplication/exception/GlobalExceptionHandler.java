package org.keni.tech.onlinecourseapplication.exception;

import org.keni.tech.onlinecourseapplication.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> invalidEmailException(InvalidEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler (InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidPasswordException (InvalidPasswordException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler (UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistException(UserAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.ALREADY_REPORTED.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body (errorResponse);
    }

    @ExceptionHandler(MobileNumberLinkedWithOtherAccountException.class)
    public ResponseEntity<ErrorResponse> mobileNumberLinkedWithSomeOtherAccountException(MobileNumberLinkedWithOtherAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.CONFLICT).body (errorResponse);
    }

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException (UserNotFoundException e) {
         ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()+"");
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body (errorResponse);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> courseNotFoundException (CourseNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler (CourseAlreadyBoughtException.class)
    public ResponseEntity<ErrorResponse> courseAlreadyBoughtException (CourseAlreadyBoughtException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.ALREADY_REPORTED.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(errorResponse);
    }

    @ExceptionHandler(CourseAlreadyAddedInTheCartException.class)
    public ResponseEntity<ErrorResponse> courseAlreadyAddedInTheCartException (CourseAlreadyAddedInTheCartException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.ALREADY_REPORTED.value(), LocalDateTime.now()+"");
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body (errorResponse);
    }

    @ExceptionHandler (SessionExpiredException.class)
    public ResponseEntity<ErrorResponse> sessionExpiredException (SessionExpiredException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now() + "");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
