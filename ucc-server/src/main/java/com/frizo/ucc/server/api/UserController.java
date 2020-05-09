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
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/check/email/verify")
    public ResponseEntity<?> checkverifyEmailCode(@CurrentUser UserPrincipal userPrincipal, @RequestParam("verifyCode") String verifyCode) {
        boolean isSuccess = userService.checkEmailVerifyCode(userPrincipal.getId(), verifyCode);
        return isSuccess ?
               ResponseEntity.ok(new ApiResponse(true, "email verify successed"))
               :
               ResponseEntity.ok(new ApiResponse(false, "email verify failed"));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/userinfo")
    public User updateUserInfo(@CurrentUser UserPrincipal userPrincipal, @RequestBody UpdateProfileRequest updateProfileRequest) throws IOException {
        User updatedUserInfo = userService.updateUserInfo(userPrincipal.getId(), updateProfileRequest);
        return updatedUserInfo;
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/avatar")
    public ResponseEntity<?> updateUserAvatar(@CurrentUser UserPrincipal userPrincipal,@RequestBody MultipartFile avatar) throws IOException {
        String avatarUrl = userService.updateUserAvatar(userPrincipal.getId(), avatar);
        return avatar != null?
                ResponseEntity.ok(new ApiResponse(true, avatarUrl))
                :
                ResponseEntity.ok(new ApiResponse(false, "avatar upload failed."));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/background")
    public ResponseEntity<?> updateProfileBackground(@CurrentUser UserPrincipal userPrincipal,@RequestBody MultipartFile background) throws IOException {
        String backgroundUrl = userService.updateProfileBackground(userPrincipal.getId(), background);
        return backgroundUrl != null?
                ResponseEntity.ok(new ApiResponse(true, backgroundUrl))
                :
                ResponseEntity.ok(new ApiResponse(false, "background upload failed."));
    }
}


