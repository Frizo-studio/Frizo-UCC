package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.response.bean.FollowingBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.following.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/send/request")
    public ResponseEntity<?> sendFollowingRequest(@RequestParam(name = "userId") Long userId,
                                                  @RequestParam(name = "fowllowingUserId") Long followingUserId){
        followingService.createFollowing(userId, followingUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "追蹤請求已送出", null));
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> cancelFollowing(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "fowllowingUserId") Long fowllowingUserId){
        followingService.deleteFollowing(userId, fowllowingUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已取消追蹤", null));
    }

    @GetMapping("/refuse/request")
    public ResponseEntity<?> refuseFollowingRequest(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "requesterId") Long requesterId){
        followingService.deleteFollowing(requesterId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已拒絕對方追蹤請求", null));
    }


    @GetMapping("/accept")
    public ResponseEntity<?> acceptFollowingRequest(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "requesterId") Long requesterId){
        followingService.acceptFollowingRequest(requesterId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已接受追蹤", null));
    }

    @GetMapping("/block")
    public ResponseEntity<?> blockFollower(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "followerId") Long followerId){
        followingService.deleteFollowing(followerId, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "已接受追蹤", null));
    }

    @GetMapping("/my/list")
    public ResponseEntity<?> findAllMyFollowingList(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "accepted", defaultValue = "true") boolean accepted){
        List<UserBean> beans = followingService.findAllMyFollowing(userId, accepted);
        return ResponseEntity.ok(new ApiResponse<>(true, "回應您的追蹤名單列表", beans));
    }

    @GetMapping("/my/followers")
    public ResponseEntity<?> findAllMyFollowers(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "accepted", defaultValue = "true") boolean accepted){
        List<UserBean> beans = followingService.findAllMyFollowers(userId, accepted);
        return ResponseEntity.ok(new ApiResponse<>(true, "回應您的追蹤者列表", beans));
    }
}
