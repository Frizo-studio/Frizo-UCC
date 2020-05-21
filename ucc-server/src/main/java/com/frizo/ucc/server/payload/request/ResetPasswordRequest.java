package com.frizo.ucc.server.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    @NotBlank(message = "Email 欄位不可為空")
    @Email
    private String email;

    @NotBlank(message = "密碼欄位不可為空")
    private String password;

    @NotBlank(message = "驗證碼碼欄位不可為空")
    private String verifyCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
