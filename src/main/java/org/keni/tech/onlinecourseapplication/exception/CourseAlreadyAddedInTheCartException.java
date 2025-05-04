package org.keni.tech.onlinecourseapplication.exception;

public class CourseAlreadyAddedInTheCartException extends RuntimeException {
    public CourseAlreadyAddedInTheCartException(String message) {
        super(message);
    }
}
