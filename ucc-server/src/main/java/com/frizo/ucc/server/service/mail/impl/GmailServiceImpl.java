package com.frizo.ucc.server.service.mail.impl;

import com.frizo.ucc.server.exception.InternalSeverErrorException;
import com.frizo.ucc.server.service.mail.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class GmailServiceImpl implements GmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Override
    public void sendEmailVerifiyCode(String to, String verifiyCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("FrizoStudio@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("UCC Email 驗證信件");
            messageHelper.setText(
                    "<p>您好，歡迎加入 UCC 體驗我們的服務，以下是您的帳號的驗證碼，請在收到信件後立即驗證，此驗證碼將在 3 分鐘後過期。</p>" +
                    "<br>" +
                    "<p>驗證碼 : <strong>" + verifiyCode + "</strong></p>" +
                    "<br>" +
                    "<p>請勿直接回覆信件，謝謝配合。",
                    true);
            mailSender.send(mimeMessage);
        } catch (Exception ex) {

            throw new InternalSeverErrorException("GmailService encounter some problems", ex);
        }
    }

    @Override
    public void sendForgotPasswordVerifiyCode(String to, String verifiyCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("FrizoStudio@gmail.com");
            messageHelper.setSubject("UCC 找回帳號驗證信件");
            messageHelper.setText(
                    "<p>您好，以下是您的找回帳號的驗證碼，請在收到信件後立即驗證，此驗證碼將在 3 分鐘後過期。</p>" +
                            "<br>" +
                            "<p>驗證碼 : <strong>" + verifiyCode + "</strong></p>" +
                            "<br>" +
                            "<p>請勿直接回覆信件，謝謝配合。",
                    true);
            messageHelper.setTo(to);
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            throw new InternalSeverErrorException("GmailService encounter some problems", ex);
        }
    }

    @Override
    public void sendFollowingRequestMessage(String to, String requesterName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("FrizoStudio@gmail.com");
            messageHelper.setSubject("UCC: 您有有新的追蹤請求申請，快來看看吧!");
            messageHelper.setText(
                    "<p>您好，" + requesterName + "向您提出了追蹤請求，快來登入答應他吧!。</p>" +
                            "<p>請勿直接回覆信件，謝謝配合。",
                    true);
            messageHelper.setTo(to);
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            throw new InternalSeverErrorException("GmailService encounter some problems", ex);
        }
    }
}
