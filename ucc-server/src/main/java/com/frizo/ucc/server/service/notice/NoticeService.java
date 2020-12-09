package com.frizo.ucc.server.service.notice;

import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.payload.response.bean.EventNoticeBean;
import com.frizo.ucc.server.payload.response.bean.NoticeBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {

    void sendUserNoticeCount(String to, UserNoticeCount userNoticeCount);

    void clearNoticeCount(NoticeType noticeType, Long userId);

    void sendEventNoticeToFollowers(Long userId, EventBean eventBean);

    List<EventNoticeBean>  findAllMyEventNoticeBySpec(Long id, int pageable);

    void readEventNotice(Long userId, Long eventNoticeId);

    void clearAllMyEventNotice(Long userId);
}
