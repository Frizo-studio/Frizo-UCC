package com.frizo.ucc.server.service.auth;

import com.frizo.ucc.server.payload.LoginRequest;
import com.frizo.ucc.server.payload.SignUpRequest;

public interface AuthService {
    String getTokenByLogin(LoginRequest request);

    Long getIdAfterRegistered(SignUpRequest request);
}
