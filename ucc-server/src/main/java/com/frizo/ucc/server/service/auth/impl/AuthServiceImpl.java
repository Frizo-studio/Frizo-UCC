package com.frizo.ucc.server.service.auth.impl;

import com.frizo.ucc.server.dao.UserRepository;
import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.exception.RequestProcessException;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.AuthProvider;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.request.ChangePasswordRequest;
import com.frizo.ucc.server.payload.request.LoginRequest;
import com.frizo.ucc.server.payload.request.ResetPasswordRequest;
import com.frizo.ucc.server.payload.request.SignUpRequest;
import com.frizo.ucc.server.security.TokenProvider;
import com.frizo.ucc.server.service.auth.AuthService;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.utils.auth.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private GmailService gmailService;

    @Override
    public String getTokenByLogin(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication);
    }

    @Override
    @Transactional
    public Long getIdAfterRegistered(SignUpRequest request) throws RequestProcessException {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RequestProcessException("Email 已被註冊使用。");
        }
        // Creating user's account
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSecurityCode(SecurityUtils.generateSecurityCode());
        User result = userRepository.save(user);
        return result.getId();
    }

    @Override
    @Transactional
    public String getEmailAfterChangePassword(Long id, ChangePasswordRequest request) throws RequestProcessException {
        User user = userRepository.getOne(id);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new RequestProcessException("舊密碼輸入錯誤，請稍後重試。");
        }
        user.setSecurityCode(SecurityUtils.generateSecurityCode());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user = userRepository.save(user);
        return user.getEmail();
    }

    @Override
    @Transactional
    public void sendForgotPasswordVerifyCodeEmail(String email) throws ResourceNotFoundException {
        StringBuilder sb = new StringBuilder();
        new Random().ints(6, 0, 10).forEach(sb::append);
        String verifyCode = sb.toString();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()){
            throw new ResourceNotFoundException("Account", "Email", email);
        }
        User user = optionalUser.get();
        user.setVerifyCode(verifyCode);
        user.setVerifyCodeUpdateAt(Instant.now());
        userRepository.save(user);
        gmailService.sendForgotPasswordVerifiyCode(user.getEmail(), user.getVerifyCode());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) throws BadRequestException, ResourceNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!optionalUser.isPresent()){
            throw new ResourceNotFoundException("Account", "Email", request.getEmail());
        }
        User user = optionalUser.get();
        Instant codeUpdatedTime = user.getVerifyCodeUpdateAt();
        Instant expiredTime = codeUpdatedTime.plusSeconds(180);
        boolean verifyStatus = false;
        if (Instant.now().isBefore(expiredTime)){
            verifyStatus = request.getVerifyCode().equals(user.getVerifyCode());
            if (verifyStatus){
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setSecurityCode(SecurityUtils.generateSecurityCode());
                user.setVerifyCode(null);
                userRepository.save(user);
            }else{
                throw new RequestProcessException("驗證碼輸入錯誤");
            }
        }else {
            throw new RequestProcessException("驗證碼時效已過期");
        }
    }

}
