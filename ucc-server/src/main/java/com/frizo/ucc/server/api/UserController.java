package com.frizo.ucc.server.api;

import com.frizo.ucc.server.payload.response.ApiResponse;
import com.frizo.ucc.server.payload.request.UpdateProfileRequest;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.security.CurrentUser;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println("Name: " + userPrincipal.getName());
        UserBean userBean = userService.getUserbyId(userPrincipal.getId());
        ApiResponse<UserBean> response = new ApiResponse<>(true, "userInfo", userBean);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('GUEST')") // 只有 GUEST 才進得來
    @GetMapping("/send/email/verify")
    public ResponseEntity<?> sendVerifyEmail(@CurrentUser UserPrincipal userPrincipal) {
        userService.sendVerifyEmail(userPrincipal.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "email 驗證信已寄出", null));
    }

    @PreAuthorize("hasRole('GUEST')") // 只有 GUEST 才進得來
    @GetMapping("/check/email/verify")
    public ResponseEntity<?> checkverifyEmailCode(@CurrentUser UserPrincipal userPrincipal, @RequestParam("verifyCode") String verifyCode) {
        boolean isSuccess = userService.checkEmailVerifyCode(userPrincipal.getId(), verifyCode);
        return isSuccess ?
                ResponseEntity.ok(new ApiResponse<>(true, "email 驗證成功", null))
                :
                ResponseEntity.ok(new ApiResponse<>(false, "email 驗證失敗", null));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/userinfo")
    public ResponseEntity<?> updateUserInfo(@CurrentUser UserPrincipal userPrincipal, @RequestBody UpdateProfileRequest updateProfileRequest) {
        UserBean updatedUserInfo = userService.updateUserInfo(userPrincipal.getId(), updateProfileRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "個人資料修改成功", updatedUserInfo));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/avatar")
    public ResponseEntity<?> updateUserAvatar(@CurrentUser UserPrincipal userPrincipal, @RequestBody MultipartFile avatar) {
        String avatarUrl = userService.updateUserAvatar(userPrincipal.getId(), avatar);
        return avatar != null ?
                ResponseEntity.ok(new ApiResponse<>(true, "Avatar 更新成功", avatarUrl))
                :
                ResponseEntity.ok(new ApiResponse<>(false, "avatar 更新失敗", null));
    }

    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    @PostMapping("/update/background")
    public ResponseEntity<?> updateProfileBackground(@CurrentUser UserPrincipal userPrincipal, @RequestBody MultipartFile background) {
        String backgroundUrl = userService.updateProfileBackground(userPrincipal.getId(), background);
        return backgroundUrl != null ?
                ResponseEntity.ok(new ApiResponse<>(true, "background 上傳成功", backgroundUrl))
                :
                ResponseEntity.ok(new ApiResponse<>(false, "background 上傳失敗", null));
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUsersByName(@RequestParam(name = "keywords") @NotBlank String keywords,
                                            @RequestParam(name = "page", defaultValue = "0") int page) {
        List<UserBean> beans = userService.findUserByKeywords(keywords, page);
        beans.forEach(b->{
            System.out.println(b.getName());
            System.out.println(b.getEmail());
        });
        return ResponseEntity.ok(new ApiResponse<>(true, "返回找到的用戶", beans));
    }

    @GetMapping("get/info")
    public ResponseEntity<?> getUserInfoById(@RequestParam(name = "id") @NotBlank Long id) {
        UserBean bean = userService.getUserbyId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "返回找到的用戶資料", bean));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/setting/auto/accept/following")
    public ResponseEntity<?> getUserInfoById(@CurrentUser UserPrincipal principal,
                                             @RequestParam(name = "isAllow") @NotBlank boolean isAllow) {
        UserBean bean = userService.updateUserActivelyAcceptFollowRequest(principal.getId(), isAllow);
        return ResponseEntity.ok(new ApiResponse<>(true, "返回找到的用戶資料", bean));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/notice/count")
    public ResponseEntity<?> getNoticeCount(@CurrentUser UserPrincipal principal){
        UserNoticeCount noticeCount = userService.getUserNoticeCount(principal.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "返回用戶的通知數量", noticeCount));
    }

}


