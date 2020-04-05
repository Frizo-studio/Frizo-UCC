package com.frizo.ucc.server.api;

import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.ApiResponse;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUserbyId(userPrincipal.getId());
    }

    @GetMapping("send/email/verify")
    public void sendVerifyEmail(@CurrentUser UserPrincipal userPrincipal) {
        userService.sendVerifyEmail(userPrincipal.getId());
    }

    @PostMapping("check/email/verify")
    public ResponseEntity<?> checkverifyEmailCode(@CurrentUser UserPrincipal userPrincipal, String userSendCode) {
        boolean isSuccess = userService.checkEmailVerifyCode(userPrincipal.getId(), userSendCode);
        return isSuccess ?
               ResponseEntity.ok(new ApiResponse(true, "email verify successed"))
               :
               ResponseEntity.ok(new ApiResponse(false, "email verify failed"));
    }
}
