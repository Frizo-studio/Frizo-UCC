package com.frizo.ucc.server.service;

import com.frizo.ucc.server.UccServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.List;
import java.util.Random;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = UccServerApplication.class)
public class MailServiceTest {
    @Autowired
    public JavaMailSender mailSender;

    @Test
    public void sendToGmail() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        messageHelper.setFrom("FrizoStudio@gmail.com");
        messageHelper.setSubject("UCC 帳號驗證測試");
        messageHelper.setText("您的驗證碼 : <strong>66577</strong>", true);
        messageHelper.setTo("Jarvan1110@gmail.com");
        mailSender.send(mimeMessage);
    }

    @Test
    public void testVerifyCode() {
        StringBuilder sb = new StringBuilder();
        new Random().ints(6, 0, 10).forEach(sb::append);
        String verifyCode = sb.toString();
        System.out.println(verifyCode);
        System.out.println(Instant.now());
    }
}