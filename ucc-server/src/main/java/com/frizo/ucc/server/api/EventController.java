package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.request.UpdateEventRequest;
import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/posted")
    public ResponseEntity<?> findMyPosted(@CurrentUser UserPrincipal principal,
                                          @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                          @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                          @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction){
        System.out.println("id: " + principal.getId());
        System.out.println("pageNumber: " + pageNumber);
        System.out.println("sortBy: " + sortBy);
        System.out.println("direction: " + direction);
        List<EventBean> beans = eventService.findmyPostedEvent(principal.getId(), pageNumber, sortBy, direction);
        return ResponseEntity.ok(new ApiResponse<>(true, "成功返回查詢結果", beans));
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteEvent(@CurrentUser UserPrincipal principal, @PathVariable("eventId") Long eventId){
        eventService.deleteEvent(principal.getId(), eventId);
        return ResponseEntity.ok(new ApiResponse<>(true, "活動刪除成功。", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<?> updateEvent(@CurrentUser UserPrincipal principal, UpdateEventRequest request){
        EventBean bean = eventService.updateEvent(principal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "活動修改成功。", bean));
    }

}
