package com.frizo.ucc.server.service.notice.impl;

import com.frizo.ucc.server.dao.notice.UserNoticeRepository;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.model.UserNotice;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.following.FollowingService;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.service.notice.NoticeType;
import com.frizo.ucc.server.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserNoticeRepository userNoticeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowingService followingService;

    @Autowired
    GmailService gmailService;

    @Override
    public void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount) {
        simpMessagingTemplate.convertAndSendToUser(to, "/topic/response", userNoticeCount);
        //simpMessagingTemplate.convertAndSend("/topic/response", userNoticeCount);
        System.out.println("訊息送出: " + to);
    }

    @Override
    public void clearNoticeCount(NoticeType noticeType, Long userId) {
        User user = userRepository.getOne(userId);
        Optional<UserNotice> noticeOptional = userNoticeRepository.findByUser(user);
        noticeOptional.ifPresent(userNotice -> {
            switch (noticeType){
                case CHAT:
                    userNotice.setChatNoticeCount(0);
                    break;

                case EVENT:
                    userNotice.setEventNotiveCount(0);
                    break;

                case FOLLOWING:
                    userNotice.setFollowingNoticeCount(0);
                    break;

                default:
                    break;
            }
            userNoticeRepository.save(userNotice);
        });
    }

    @Override
    public void sendEventNoticeToFollowers(Long userId, EventBean eventBean) {
        List<UserBean> followers = followingService.findAllMyFollowers(userId, true);
        // 處理寄信邏輯 -------
        gmailService.sendEventNoticeToFollowers(followers, eventBean);
        // -------------------

//        followers.forEach(user -> {
//            String user =
//        });
//
//        // 處理站內推波邏輯 -------
//        followers.forEach(user -> {
//            simpMessagingTemplate.convertAndSendToUser(user.getEmail(),);
//        });
//        // -------------------
    }
}
