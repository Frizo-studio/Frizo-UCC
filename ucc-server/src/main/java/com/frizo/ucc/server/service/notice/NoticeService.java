package com.frizo.ucc.server.service.notice;

import com.frizo.ucc.server.payload.response.UserNoticeCount;

public interface NoticeService {

    void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount);

    void clearNoticeCount(NoticeType noticeType, Long userId);

}
