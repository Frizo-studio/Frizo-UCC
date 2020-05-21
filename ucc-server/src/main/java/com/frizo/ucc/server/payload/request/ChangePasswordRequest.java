package com.frizo.ucc.server.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest {
    @NotBlank(message="舊密碼欄位不可為空")
    private String oldPassword;

    @NotBlank(message="新密碼欄位不可為空")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
