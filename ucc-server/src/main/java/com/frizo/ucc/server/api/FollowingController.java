package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.bean.FollowingBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.following.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/following")
public class FollowingController {
    @Autowired
    FollowingService followingService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/send/request")
    public ResponseEntity<?> sendFollowingRequest(@CurrentUser UserPrincipal principal,
                                                  @RequestParam(name = "fowllowingUserId") Long followingUserId){
        Long userId = principal.getId();
        followingService.createFollowing(userId, followingUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "追蹤請求已送出", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelFollowing(@CurrentUser UserPrincipal principal,
                                             @RequestParam(name = "fowllowingUserId") Long fowllowingUserId){
        Long userId = principal.getId();
        followingService.deleteFollowing(userId, fowllowingUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已取消追蹤", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/refuse/request")
    public ResponseEntity<?> refuseFollowingRequest(@CurrentUser UserPrincipal principal,
                                                    @RequestParam(name = "requesterId") Long requesterId){
        Long userId = principal.getId();
        followingService.deleteFollowing(requesterId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已拒絕對方追蹤請求", null));
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/accept")
    public ResponseEntity<?> acceptFollowingRequest(@CurrentUser UserPrincipal principal,
                                                    @RequestParam(name = "requesterId") Long requesterId){
        Long userId = principal.getId();
        followingService.acceptFollowingRequest(requesterId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已接受追蹤", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/block")
    public ResponseEntity<?> blockFollower(@CurrentUser UserPrincipal principal,
                                                    @RequestParam(name = "followerId") Long followerId){
        Long userId = principal.getId();
        followingService.deleteFollowing(followerId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已封鎖該追蹤", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/list")
    public ResponseEntity<?> findAllMyFollowingList(@CurrentUser UserPrincipal principal,
                                                    @RequestParam(name = "accepted", defaultValue = "true") boolean accepted){
        Long userId = principal.getId();
        List<UserBean> beans = followingService.findAllMyFollowing(userId, accepted);
        return ResponseEntity.ok(new ApiResponse<>(true, "回應您的追蹤名單列表", beans));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/followers")
    public ResponseEntity<?> findAllMyFollowers(@CurrentUser UserPrincipal principal,
                                                    @RequestParam(name = "accepted", defaultValue = "true") boolean accepted){
        Long userId = principal.getId();
        List<UserBean> beans = followingService.findAllMyFollowers(userId, accepted);
        return ResponseEntity.ok(new ApiResponse<>(true, "回應您的追蹤者列表", beans));
    }
}
