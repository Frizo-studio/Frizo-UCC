package com.frizo.ucc.server.service.auth;

import com.frizo.ucc.server.payload.request.ChangePasswordRequest;
import com.frizo.ucc.server.payload.request.LoginRequest;
import com.frizo.ucc.server.payload.request.ResetPasswordRequest;
import com.frizo.ucc.server.payload.request.SignUpRequest;

public interface AuthService {
    String getTokenByLogin(LoginRequest request);

    Long getIdAfterRegistered(SignUpRequest request);

    String getEmailAfterChangePassword(Long id, ChangePasswordRequest changePasswordRequest);

    void sendForgotPasswordVerifyCodeEmail(String email);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
