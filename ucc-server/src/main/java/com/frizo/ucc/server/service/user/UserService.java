package com.frizo.ucc.server.service.user;

import com.frizo.ucc.server.payload.request.UpdateProfileRequest;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserBean getUserbyId(Long id);

    void sendVerifyEmail(Long id);

    Boolean checkEmailVerifyCode(Long id, String userSendCode);

    UserBean updateUserInfo(Long userId, UpdateProfileRequest updateProfileRequest);

    String updateUserAvatar(Long id, MultipartFile avatar);

    String updateProfileBackground(Long id, MultipartFile background);
}
