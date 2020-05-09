package com.frizo.ucc.server.service.auth;

import com.frizo.ucc.server.payload.ChangePasswordRequest;
import com.frizo.ucc.server.payload.LoginRequest;
import com.frizo.ucc.server.payload.ResetPasswordRequest;
import com.frizo.ucc.server.payload.SignUpRequest;

public interface AuthService {
    String getTokenByLogin(LoginRequest request);

    Long getIdAfterRegistered(SignUpRequest request);

    String getEmailAfterChangePassword(Long id, ChangePasswordRequest changePasswordRequest);

    void sendForgotPasswordVerifyCodeEmail(String email);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
