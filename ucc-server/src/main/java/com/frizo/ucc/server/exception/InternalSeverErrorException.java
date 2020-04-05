package com.frizo.ucc.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalSeverErrorException extends RuntimeException {

    public InternalSeverErrorException(String message) {
        super(message);
    }

    public InternalSeverErrorException(String message, Throwable cause) {
        super(message, cause);
    }


}
