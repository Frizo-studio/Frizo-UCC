package com.frizo.ucc.server.service.following;

import com.frizo.ucc.server.payload.response.bean.FollowingBean;
import com.frizo.ucc.server.payload.response.bean.UserBean;

import java.util.List;

public interface FollowingService {

    List<UserBean> findAllMyFollowing(Long userId, boolean accepted);

    List<UserBean> findAllMyFollowers(Long userId, boolean accepted);

    void acceptFollowingRequest(Long requesterId, Long followingUserId);

    void createFollowing(Long requesterId, Long followingUserId);

    void deleteFollowing(Long requesterId, Long followingUserId);
}
