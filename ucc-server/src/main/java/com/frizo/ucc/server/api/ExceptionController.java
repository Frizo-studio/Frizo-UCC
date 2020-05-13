package com.frizo.ucc.server.api;

import com.frizo.ucc.server.exception.*;
import com.frizo.ucc.server.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({RequestProcessException.class})
    public final ResponseEntity<?> handleRequestProcessException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({BadRequestException.class})
    public final ResponseEntity<?> handleBadRequestException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({InternalSeverErrorException.class})
    public final ResponseEntity<?> handleInternalSeverException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({OAuth2AuthenticationProcessingException.class})
    public final ResponseEntity<?> handleOAuth2AuthenticationException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<?> handleResourceNotFoundException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({AuthenticationException.class})
    public final ResponseEntity<?> handleAuthenticationErrorException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }


}
