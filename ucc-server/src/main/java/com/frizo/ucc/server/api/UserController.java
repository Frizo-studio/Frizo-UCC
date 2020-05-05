package com.frizo.ucc.server.api;

import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.ApiResponse;
import com.frizo.ucc.server.payload.UpdateProfileRequest;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUserbyId(userPrincipal.getId());
    }

    @PreAuthorize("hasRole('GUEST')") // 只有 GUEST 才進得來
    @GetMapping("/send/email/verify")
    public void sendVerifyEmail(@CurrentUser UserPrincipal userPrincipal) {
        userService.sendVerifyEmail(userPrincipal.getId());
    }

    @PreAuthorize("hasRole('GUEST')") // 只有 GUEST 才進得來
    @PostMapping("/check/email/verify")
    public ResponseEntity<?> checkverifyEmailCode(@CurrentUser UserPrincipal userPrincipal, @RequestBody String verifyCode) {
        boolean isSuccess = userService.checkEmailVerifyCode(userPrincipal.getId(), verifyCode);
        return isSuccess ?
               ResponseEntity.ok(new ApiResponse(true, "email verify successed"))
               :
               ResponseEntity.ok(new ApiResponse(false, "email verify failed"));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/userinfo")
    public User updateUserInfo(@CurrentUser UserPrincipal userPrincipal, UpdateProfileRequest updateProfileRequest) throws IOException {
        User updatedUserInfo = userService.updateUserInfo(userPrincipal.getId(), updateProfileRequest);
        return updatedUserInfo;
    }
}


