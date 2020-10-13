package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.notice.NoticeService;
import com.frizo.ucc.server.service.notice.NoticeType;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/notice")
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
}
