package com.frizo.ucc.server.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="user_notice")
public class UserNotice implements Serializable {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private int followingNoticeCount;

    private int eventNotiveCount;

    private int chatNoticeCount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFollowingNoticeCount() {
        return followingNoticeCount;
    }

    public void setFollowingNoticeCount(int followingNoticeCount) {
        this.followingNoticeCount = followingNoticeCount;
    }

    public int getEventNotiveCount() {
        return eventNotiveCount;
    }

    public void setEventNotiveCount(int eventNotiveCount) {
        this.eventNotiveCount = eventNotiveCount;
    }

    public int getChatNoticeCount() {
        return chatNoticeCount;
    }

    public void setChatNoticeCount(int chatNoticeCount) {
        this.chatNoticeCount = chatNoticeCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
