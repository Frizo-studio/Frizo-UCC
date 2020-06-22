package com.frizo.ucc.server.model;

import java.io.Serializable;
import java.util.Objects;

public class FollowingPrimarykey implements Serializable {

    private Long userId;

    private Long followingUserId;

    public FollowingPrimarykey() {
    }

    public FollowingPrimarykey(Long userId, Long followingUserId) {
        this.userId = userId;
        this.followingUserId = followingUserId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowingPrimarykey that = (FollowingPrimarykey) o;
        return getUserId().equals(that.getUserId()) &&
                getFollowingUserId().equals(that.getFollowingUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFollowingUserId());
    }
}
