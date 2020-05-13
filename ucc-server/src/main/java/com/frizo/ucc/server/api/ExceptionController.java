package com.frizo.ucc.server.api;

import com.frizo.ucc.server.exception.*;
import com.frizo.ucc.server.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({RequestProcessException.class})
    public final ResponseEntity<?> handleRequestProcessException(Exception ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({BadRequestException.class})
    public final ResponseEntity<?> handleBadRequestException(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({InternalSeverErrorException.class})
    public final ResponseEntity<?> handleInternalSeverException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({OAuth2AuthenticationProcessingException.class})
    public final ResponseEntity<?> handleOAuth2AuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<?> handleResourceNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public final ResponseEntity<?> handleBadCredentialsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "密碼輸入錯誤。", null));
    }

    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
    public final ResponseEntity<?> handleAuthenticationNotFoundErrorException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                .body(new ApiResponse<>(false, "登入憑證已失效，請重新登入。", null));
    }

    @ExceptionHandler({UnsupportedMediaTypeException.class})
    public final ResponseEntity<?> handleUnsupportedMediaTypeException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<?> handleMethodArgumentNotValidException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "欄位輸入不合法。", null));
    }

}
