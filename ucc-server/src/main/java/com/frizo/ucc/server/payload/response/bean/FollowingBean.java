package com.frizo.ucc.server.payload.response.bean;

public class FollowingBean {

    private Long userId;

    private Long followingUserId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
    }
}
