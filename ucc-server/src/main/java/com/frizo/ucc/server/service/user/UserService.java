package com.frizo.ucc.server.service.user;

import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User getUserbyId(Long id);

    void sendVerifyEmail(Long id);

    Boolean checkEmailVerifyCode(Long id, String userSendCode);

    User updateUserInfo(Long userId, UpdateProfileRequest updateProfileRequest) throws IOException;

    String updateUserAvatar(Long id, MultipartFile avatar);

    String updateProfileBackground(Long id, MultipartFile background);
}
