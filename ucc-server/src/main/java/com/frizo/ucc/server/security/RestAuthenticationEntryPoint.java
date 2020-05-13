package com.frizo.ucc.server.security;

import com.frizo.ucc.server.exception.RequestProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// token filter 認證之敗後進入此 EntryPoint

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", ex.getMessage());
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登入憑證已失效，請重新登入。");
    }
}
