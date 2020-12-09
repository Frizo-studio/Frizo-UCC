package com.frizo.ucc.server.service.mail.impl;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.exception.InternalSeverErrorException;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.mail.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GmailServiceImpl implements GmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppProperties appProperties;

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

    @Override
    public void sendEventNoticeToFollowers(List<UserBean> users, EventBean eventBean) {
        new Thread(()-> {
            String dmDir = appProperties.getFileDir().getDmDir();
            String[] urlSplit = eventBean.getDmUrl().split("/");
            String fileName = urlSplit[urlSplit.length-1];
            String filePath = dmDir + "/" + fileName;

            List<String> emailList = users.stream()
                    .map(UserBean::getEmail)
                    .collect(Collectors.toList());

            String[] targets = new String[emailList.size()];

            for(int i = 0; i < emailList.size(); i++){
                targets[i] = emailList.get(i);
            }

            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom("FrizoStudio@gmail.com");
                messageHelper.setSubject("UCC: 您有新的訊息!");
                messageHelper.setText(
                        "<p>" + eventBean.getPosterName() +" 剛剛發布了一個新的活動喔!。</p>" +
                                "<p>" + "活動名稱: " + eventBean.getTitle() + "</p>" +
                                "<p>" + "活動說明: " + eventBean.getDescription() + "</p>" +
                                "<p>更多詳細內容請至站內察看喔。</p>" +
                                "<p>請勿直接回覆信件，謝謝配合。",
                        true);

                FileSystemResource file = new FileSystemResource(filePath);
                messageHelper.addAttachment(eventBean.getTitle()+".jpg", file);
                messageHelper.setTo(targets);
                mailSender.send(mimeMessage);
            } catch (Exception ex) {
                throw new InternalSeverErrorException("GmailService encounter some problems", ex);
            }
        }).start();
    }
}
