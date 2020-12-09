package com.frizo.ucc.server.payload.response.bean;

import java.time.Instant;

public class EventNoticeBean {

    private Long eventId;

    private Long eventNoticeId;

    private Long posterId;

    private String posterName;

    private String posterAvaterUrl;

    private String message;

    private boolean readed;

    private Instant createdAt;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getPosterId() {
        return posterId;
    }

    public void setPosterId(Long posterId) {
        this.posterId = posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterAvaterUrl() {
        return posterAvaterUrl;
    }

    public void setPosterAvaterUrl(String posterAvaterUrl) {
        this.posterAvaterUrl = posterAvaterUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public Long getEventNoticeId() {
        return eventNoticeId;
    }

    public void setEventNoticeId(Long eventNoticeId) {
        this.eventNoticeId = eventNoticeId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
