package com.frizo.ucc.server.service.user.impl;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.dao.UserRepository;
import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.exception.InternalSeverErrorException;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.UpdateProfileRequest;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.service.user.UserService;
import com.frizo.ucc.server.utils.files.FrizoFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GmailService gmailService;

    @Autowired
    private AppProperties appProperties;

    @Override
    public User getUserbyId(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    @Transactional
    public void sendVerifyEmail(Long id) {
        StringBuilder sb = new StringBuilder();
        new Random().ints(6, 0, 10).forEach(sb::append);
        String verifyCode = sb.toString();
        User user = userRepository.getOne(id);
        user.setVerifyCode(verifyCode);
        user.setVerifyCodeUpdateAt(Instant.now());
        userRepository.save(user);
        gmailService.sendEmailVerifiyCode(user.getEmail(), user.getVerifyCode());
    }

    @Override
    @Transactional
    public Boolean checkEmailVerifyCode(Long id, String userSendCode) {
        User user = userRepository.getOne(id);
        String verifyCode = user.getVerifyCode();
        Instant codeUpdatedTime = user.getVerifyCodeUpdateAt();
        Instant expiredTime = codeUpdatedTime.plusSeconds(180);
        boolean verifyStatus = false;
        if (Instant.now().isBefore(expiredTime)){
            verifyStatus = userSendCode.equals(verifyCode);
            if (verifyStatus) {
                user.setEmailVerified(true);
                user.setVerifyCode(null);
                userRepository.save(user);
            }
        }
        return verifyStatus;
    }

    @Override
    public User updateUserInfo(Long id, UpdateProfileRequest updateProfileRequest) throws IOException {
        if (updateProfileRequest == null){
            throw new BadRequestException("您未上傳任何資料。");
        }
        User user = userRepository.getOne(id);
        user.setName(updateProfileRequest.getName());
        user.setAddress(updateProfileRequest.getAddress());
        user.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        user.setCollageLocation(updateProfileRequest.getCollageLocation());
        user.setCollageName(updateProfileRequest.getCollageName());
        user.setGrade(updateProfileRequest.getGrade());
        user.setMajorSubject(updateProfileRequest.getMajorSubject());
        user.setGender(updateProfileRequest.getGender());
        return userRepository.save(user);
    }

    @Override
    public String updateUserAvatar(Long id, MultipartFile avatar) {
        if (avatar == null || avatar.isEmpty()){
            throw new BadRequestException("您並未上傳頭像文件");
        }
        User user = userRepository.getOne(id);
        try{
            String avatarName = FrizoFileUtils.storePhoto(avatar, appProperties.getFileDir().getAvatarDir());
            String avatarUrl = appProperties.getFileDir().getAvatarBaseUrl() + avatarName;
            user.setImageUrl(avatarUrl);
            userRepository.save(user);
        }catch (Exception ex){
            throw new InternalSeverErrorException("文件伺服器發生錯誤問題", ex);
        }
        return user.getImageUrl();
    }

    @Override
    public String updateProfileBackground(Long id, MultipartFile background) {
        if (background == null || background.isEmpty()){
            throw new BadRequestException("您並未上傳背景圖文件");
        }
        User user = userRepository.getOne(id);
        try{
            String bgName = FrizoFileUtils.storePhoto(background, appProperties.getFileDir().getBackgroundDir());
            String bgUrl = appProperties.getFileDir().getBackgroundBaseUrl() + bgName;
            user.setBackgroundUrl(bgUrl);
            userRepository.save(user);
        }catch (Exception ex){
            throw new InternalSeverErrorException("文件伺服器發生錯誤問題", ex);
        }
        return user.getBackgroundUrl();
    }
}
