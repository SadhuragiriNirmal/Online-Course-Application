package org.keni.tech.onlinecourseapplication.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String errorMessage;
    private int statusCode;
    private String timestamp;
    public ErrorResponse(String errorMessage, int statusCode, String timestamp) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }
}
