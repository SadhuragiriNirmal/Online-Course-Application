package org.keni.tech.onlinecourseapplication.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStructure<T> {
    private final String message;
    private final int status;
    private T data;
    public ResponseStructure(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}