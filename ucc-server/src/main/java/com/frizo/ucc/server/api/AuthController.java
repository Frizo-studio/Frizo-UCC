package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.request.ChangePasswordRequest;
import com.frizo.ucc.server.payload.request.LoginRequest;
import com.frizo.ucc.server.payload.request.ResetPasswordRequest;
import com.frizo.ucc.server.payload.request.SignUpRequest;
import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.AuthPayload;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.getTokenByLogin(loginRequest);
        ApiResponse<AuthPayload> response = new ApiResponse<>(true, "登入成功", new AuthPayload(token));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long userId = authService.getIdAfterRegistered(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userId).toUri();
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true, "註冊成功", null));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid ChangePasswordRequest request) {
        String email = authService.getEmailAfterChangePassword(userPrincipal.getId(), request);
        LoginRequest changePasswdRequest = new LoginRequest();
        changePasswdRequest.setEmail(email);
        changePasswdRequest.setPassword(request.getNewPassword());
        String newToken = authService.getTokenByLogin(changePasswdRequest);
        ApiResponse<AuthPayload> response = new ApiResponse<>(true, "密碼修該成功", new AuthPayload(newToken));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/send/forget/password/verify")
    public ResponseEntity<?> sendVerifyEmail(@RequestParam("email") @Email String email) {
        authService.sendForgotPasswordVerifyCodeEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "修改密碼驗證信已寄出", null));
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "密碼重製完成", null));
    }
}
