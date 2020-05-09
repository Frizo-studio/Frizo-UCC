package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.*;
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
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            Long userId = authService.getIdAfterRegistered(signUpRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/user/me")
                    .buildAndExpand(userId).toUri();
            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "註冊成功。"));
        }catch (Exception ex){
            return ResponseEntity.ok(new ApiResponse(false, ex.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal userPrincipal,@RequestBody @Valid ChangePasswordRequest request) {
        try{
            String email = authService.getEmailAfterChangePassword(userPrincipal.getId(), request);
            LoginRequest changePasswdRequest = new LoginRequest();
            changePasswdRequest.setEmail(email);
            changePasswdRequest.setPassword(request.getNewPassword());
            String newToken = authService.getTokenByLogin(changePasswdRequest);
            return ResponseEntity.ok(new AuthResponse(newToken));
        }catch (Exception ex){
            return ResponseEntity.ok(new ApiResponse(false, ex.getMessage()));
        }
    }

    @GetMapping("/send/forget/password/verify")
    public ApiResponse sendVerifyEmail(@RequestParam("email") @Email String email) {
        try{
            authService.sendForgotPasswordVerifyCodeEmail(email);
        }catch (Exception ex){
            return new ApiResponse(false, ex.getMessage());
        }
        return new ApiResponse(true, "verify email already sended.");
    }

    @PostMapping("/reset/password")
    public ApiResponse resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        try {
            authService.resetPassword(resetPasswordRequest);
        }catch (Exception ex){
            return new ApiResponse(false, ex.getMessage());
        }
        return new ApiResponse(true, "password has been changed.");
    }
}
