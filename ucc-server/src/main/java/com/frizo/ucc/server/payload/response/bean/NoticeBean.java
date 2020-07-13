package com.frizo.ucc.server.payload.response.bean;

public class NoticeBean {

    private NoticeType noticeType;

    private String description;

    public static enum NoticeType{
        FOLLOWING, EVENT
    }
}
