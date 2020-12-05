package com.frizo.ucc.server.service.notice;

import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.NoticeBean;

public interface NoticeService {

    void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount);

    void clearNoticeCount(NoticeType noticeType, Long userId);

    void sendEventNoticeToFollowers(Long userId, EventBean eventBean);
}
