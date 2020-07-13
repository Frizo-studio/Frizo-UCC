package com.frizo.ucc.server.payload.response;

public class UserNoticeCount {

    private int followingNoticeCount;

    private int eventNotiveCount;

    private int chatNoticeCount;

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
}
