package com.frizo.ucc.server.service.user;

import com.frizo.ucc.server.model.User;

public interface UserService {
    User getUserbyId(Long id);

    void sendVerifyEmail(Long id);

    Boolean checkEmailVerifyCode(Long id, String userSendCode);
}
