package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createEvent(@CurrentUser UserPrincipal userPrincipal, CreateEventRequest request) {
        System.out.println(request.getTitle());
        System.out.println(request.getDescription());
        request.getLabelNameList().forEach(label -> {
            System.out.println("標籤: " + label);
        });
        System.out.println(request.getMaxNumberOfPeople());
        System.out.println(request.getEventStartTime());
        System.out.println(request.getRegistrationDeadline());
        System.out.println(request.getPlace());
        EventBean bean = eventService.createEvent(userPrincipal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "活動建立成功", null));
    }

}
