package com.frizo.ucc.server.service.user;

import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.UserInfo;

public interface UserService {
    User getUserbyId(Long id);

    void sendVerifyEmail(Long id);

    Boolean checkEmailVerifyCode(Long id, String userSendCode);

    void updateUserInfo(Long userId, UserInfo userInfo);
}
