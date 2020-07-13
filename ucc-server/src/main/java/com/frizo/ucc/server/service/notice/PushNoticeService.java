package com.frizo.ucc.server.service.notice;

import com.frizo.ucc.server.payload.response.UserNoticeCount;

public interface PushNoticeService {

    void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount);

}
