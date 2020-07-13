package com.frizo.ucc.server.service.user;

import com.frizo.ucc.server.payload.request.UpdateProfileRequest;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserBean getUserbyId(Long id);

    void sendVerifyEmail(Long id);

    Boolean checkEmailVerifyCode(Long id, String userSendCode);

    UserBean updateUserInfo(Long userId, UpdateProfileRequest updateProfileRequest);

    String updateUserAvatar(Long id, MultipartFile avatar);

    String updateProfileBackground(Long id, MultipartFile background);

    List<UserBean> findUserByKeywords(String keywords, int page);

    UserBean updateUserActivelyAcceptFollowRequest(Long id, boolean isAllow);

    UserNoticeCount getUserNoticeCount(Long id);
}
