package com.frizo.ucc.server.service.user.impl;

import com.frizo.ucc.server.dao.UserRepository;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GmailService gmailService;

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
    public Boolean checkEmailVerifyCode(Long id, String userSendCode) {
        User user = userRepository.getOne(id);
        String verifyCode = user.getVerifyCode();
        Instant codeUpdatedTime = user.getVerifyCodeUpdateAt();
        Instant expiredTime = codeUpdatedTime.plusSeconds(180);
        boolean verifyStatus = false;
        if (Instant.now().isBefore(expiredTime)){
            verifyStatus = userSendCode.equals(verifyCode);
        }
        return verifyStatus;
    }
}
