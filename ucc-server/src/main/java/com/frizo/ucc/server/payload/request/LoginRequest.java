package com.frizo.ucc.server.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email 欄位不可為空")
    @Email(message = "您的輸入不符合 Email 格式")
    private String email;

    @NotBlank(message = "密碼欄位不可為空")
    private String password;

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
}
