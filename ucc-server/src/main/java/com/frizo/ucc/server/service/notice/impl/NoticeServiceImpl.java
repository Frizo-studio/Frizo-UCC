package com.frizo.ucc.server.service.notice.impl;

import com.frizo.ucc.server.dao.event.EventRepository;
import com.frizo.ucc.server.dao.notice.EventNoticeRepository;
import com.frizo.ucc.server.dao.notice.UserNoticeRepository;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.model.Event;
import com.frizo.ucc.server.model.EventNotice;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.model.UserNotice;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.EventNoticeBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.following.FollowingService;
import com.frizo.ucc.server.service.mail.GmailService;
import com.frizo.ucc.server.service.notice.NoticeType;
import com.frizo.ucc.server.service.notice.NoticeService;
import com.frizo.ucc.server.utils.common.PageRequestBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserNoticeRepository userNoticeRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowingService followingService;

    @Autowired
    EventNoticeRepository eventNoticeRepository;

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
            switch (noticeType) {
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

        createEventNoticeMessageToFollowers(userId, eventBean.getId(), followers);

        // 處理寄信邏輯 -------
        gmailService.sendEventNoticeToFollowers(followers, eventBean);
        // -------------------

        // 取出 userNoticeCount，用 map 裝 Map<Email, UserNotiveCount>
        Map<String, UserNotice> emailMapNoticeCount = new HashMap<>();

        followers.forEach(user -> {

            Optional<UserNotice> noticeOption = userNoticeRepository.findById(user.getId());
            noticeOption.ifPresentOrElse(notice -> {
                        notice.setEventNotiveCount(notice.getEventNotiveCount() + 1);
                        userNoticeRepository.save(notice);
                        emailMapNoticeCount.put(user.getEmail(), notice);
                    },
                    () -> {
                        UserNotice notice = new UserNotice();
                        notice.setEventNotiveCount(1);
                        notice.setFollowingNoticeCount(0);
                        notice.setChatNoticeCount(0);
                        notice.setId(userId);
                        userNoticeRepository.save(notice);
                        emailMapNoticeCount.put(user.getEmail(), notice);
                    });
        });

        // 處理站內推波邏輯 -------
        emailMapNoticeCount.forEach((email, notice) -> {
            UserNoticeCount userNoticeCount = new UserNoticeCount();
            BeanUtils.copyProperties(notice, userNoticeCount);
            simpMessagingTemplate.convertAndSendToUser(email, "/topic/response", userNoticeCount);
        });
        // -------------------
    }

    private void createEventNoticeMessageToFollowers(Long userId, Long eventId, List<UserBean> followers) {
        Event event = eventRepository.getOne(eventId);
        System.out.println("event in createEventNoticeMessageToFollowers: " + event.getTitle());
        User poster = userRepository.getOne(userId);
        System.out.println("posterName: " + poster.getName());
        followers.forEach(follower -> {
            User user = userRepository.getOne(follower.getId());
            System.out.println("追蹤者: " + user.getName());
            EventNotice eventNotice = new EventNotice();
            eventNotice.setEvent(event);
            eventNotice.setUser(user);
            eventNotice.setReaded(false);
            eventNotice.setCreatedAt(Instant.now());
            eventNotice.setUpdatedAt(Instant.now());
            eventNotice.setCreatedBy(poster.getId());
            eventNotice.setUpdatedBy(poster.getId());
            eventNotice.setMessage(poster.getName() + "剛剛發布了新的活動: " + event.getTitle() + "，快來看看吧!");
            eventNoticeRepository.save(eventNotice);
        });
    }

    @Override
    public List<EventNoticeBean> findAllMyEventNoticeBySpec(Long id, int page) {
        Pageable pageRequest = PageRequestBuilder.create()
                .pageNumber(page)
                .pageSize(10)
                .build();

        User user = userRepository.getOne(id);
        Page<EventNotice> eventNoticePage = eventNoticeRepository.findAllByUser(user, pageRequest);
        List<EventNoticeBean> eventNoticeBeanList = new ArrayList<>();
        eventNoticePage.forEach(eNotice -> {
            User poster = userRepository.getOne(eNotice.getCreatedBy());
            EventNoticeBean bean = new EventNoticeBean();
            bean.setEventId(eNotice.getEvent().getId());
            bean.setEventNoticeId(eNotice.getId());
            bean.setMessage(eNotice.getMessage());
            bean.setPosterId(poster.getId());
            bean.setPosterAvaterUrl(poster.getImageUrl());
            bean.setPosterName(poster.getName());
            bean.setReaded(eNotice.isReaded());
            bean.setCreatedAt(eNotice.getCreatedAt());
            eventNoticeBeanList.add(bean);
        });
        return eventNoticeBeanList;
    }

    @Override
    public void readEventNotice(Long userId, Long eventNoticeId) {
        User user = userRepository.getOne(userId);
        EventNotice eventNotice = eventNoticeRepository.getOne(eventNoticeId);
        eventNotice.setReaded(true);
        Optional<UserNotice> userNoticeOptional = userNoticeRepository.findByUser(user);
        userNoticeOptional.ifPresent(notice -> {
            int eventNoticeCount = notice.getEventNotiveCount();
            if (eventNoticeCount > 0) {
                eventNoticeCount = eventNoticeCount - 1;
                notice.setEventNotiveCount(eventNoticeCount);
                UserNoticeCount userNoticeCount = new UserNoticeCount();
                BeanUtils.copyProperties(notice, userNoticeCount);
                userNoticeRepository.save(notice);
                simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/topic/response", userNoticeCount);
            }
        });
        eventNoticeRepository.save(eventNotice);
    }

    @Override
    @Transactional
    public void clearAllMyEventNotice(Long userId) {
        User user = userRepository.getOne(userId);
        eventNoticeRepository.deleteAllByUser(user);
    }
}
