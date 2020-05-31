package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createEvent(@CurrentUser UserPrincipal userPrincipal, CreateEventRequest request) {
        EventBean bean = eventService.createEvent(userPrincipal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "活動建立成功", bean));
    }

    @PostMapping("/find")
    public ResponseEntity<?> findEvent(@RequestBody QueryEventRequest request) {
        System.out.println("keywords: " + request.getKeywords());
        List<EventBean> beans = eventService.findAllByQuerySpec(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "成功返回查詢結果", beans));
    }

}
