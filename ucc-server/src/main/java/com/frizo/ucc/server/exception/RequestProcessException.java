package com.frizo.ucc.server.exception;

public class RequestProcessException extends RuntimeException {
    public RequestProcessException(String message) {
        super(message);
    }

    public RequestProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
