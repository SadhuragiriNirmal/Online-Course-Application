package org.keni.tech.onlinecourseapplication.exception;

public class CourseAlreadyBoughtException extends RuntimeException {
    public CourseAlreadyBoughtException(String message) {
        super(message);
    }
}
