package com.frizo.ucc.server.service.user.impl;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.exception.InternalSeverErrorException;
import com.frizo.ucc.server.exception.RequestProcessException;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.request.UpdateProfileRequest;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.service.user.UserService;
import com.frizo.ucc.server.utils.common.PageRequestBuilder;
import com.frizo.ucc.server.utils.files.FrizoFileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public UserBean getUserbyId(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        UserBean bean = new UserBean();
        BeanUtils.copyProperties(user, bean);
        return bean;
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
    public UserBean updateUserInfo(Long id, UpdateProfileRequest updateProfileRequest){
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
        user = userRepository.save(user);
        UserBean bean = new UserBean();
        BeanUtils.copyProperties(user, bean);
        return bean;
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
            throw new RequestProcessException(ex.getMessage());
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

    @Override
    public List<UserBean> findUserByKeywords(String keywords, int page) {
        Pageable pageRequest = PageRequestBuilder.create()
                .pageNumber(page)
                .pageSize(10)
                .build();
        Page<User> users = userRepository.findAllByEmailOrNameContains(keywords, keywords, pageRequest);
        List<UserBean> userBeanList = new ArrayList<>();
        users.forEach(user -> {
            UserBean bean = new UserBean();
            BeanUtils.copyProperties(user, bean);
            userBeanList.add(bean);
        });
        return userBeanList;
    }

    @Override
    public UserBean updateUserActivelyAcceptFollowRequest(Long id, boolean isAllow) {
        User user = userRepository.getOne(id);
        user.setActivelyAcceptFollowRequest(isAllow);
        user = userRepository.save(user);
        UserBean bean = new UserBean();
        BeanUtils.copyProperties(user, bean);
        return bean;
    }
}
