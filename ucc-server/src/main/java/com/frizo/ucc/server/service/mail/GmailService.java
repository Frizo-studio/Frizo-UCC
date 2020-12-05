package com.frizo.ucc.server.service.mail;

import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;

import java.util.List;

public interface GmailService {
    void sendEmailVerifiyCode(String to, String verifiyCode);

    void sendForgotPasswordVerifiyCode(String to, String verifiyCode);

    void sendFollowingRequestMessage(String to, String requesterName);

    void sendEventNoticeToFollowers(List<UserBean> users, EventBean eventBean);
}
