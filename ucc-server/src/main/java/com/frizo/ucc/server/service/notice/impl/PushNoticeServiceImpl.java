package com.frizo.ucc.server.service.notice.impl;

import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.service.notice.PushNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushNoticeServiceImpl implements PushNoticeService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount) {
        simpMessagingTemplate.convertAndSendToUser(to, "/topic/response", userNoticeCount);
        //simpMessagingTemplate.convertAndSend("/topic/response", userNoticeCount);
        System.out.println("訊息送出: " + to);
    }
}
