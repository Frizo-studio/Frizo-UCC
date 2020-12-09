package com.frizo.ucc.server.api;

import com.frizo.ucc.server.model.EventNotice;
import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.EventNoticeBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.notice.NoticeService;
import com.frizo.ucc.server.service.notice.NoticeType;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @GetMapping("/clear/{noticeType}")
    @PreAuthorize("hasRole('USER')")
    public void clearNoticeCount(@PathVariable("noticeType") NoticeType noticeType,
                                 @CurrentUser UserPrincipal principal){
        noticeService.clearNoticeCount(noticeType, principal.getId());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNoticeCount(@CurrentUser UserPrincipal principal){
        UserNoticeCount noticeCount = userService.getUserNoticeCount(principal.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "返回 user 消息數量", noticeCount));
    }

    @GetMapping("/find/my/event/notice/message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findMyEventNoticeMessage(@CurrentUser UserPrincipal principal, @RequestParam("page") Integer page){
        List<EventNoticeBean> eventNoticeBeans = noticeService.findAllMyEventNoticeBySpec(principal.getId(), page);
        return ResponseEntity.ok(new ApiResponse<>(true, "返回 user 活動消息", eventNoticeBeans));
    }

    @GetMapping("/read/event/message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> readEventMessage(@CurrentUser UserPrincipal principal, @RequestParam("eventNoticeId") Long eventNoticeId) {
        noticeService.readEventNotice(principal.getId(), eventNoticeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已讀訊息: " + eventNoticeId, null));
    }

    @GetMapping("/clear/all/my/event/message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> clearAllEventMessage(@CurrentUser UserPrincipal principal) {
        noticeService.clearAllMyEventNotice(principal.getId());
        noticeService.clearNoticeCount(NoticeType.EVENT, principal.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "已清空所有活動消息: ", null));
    }

}
