package com.frizo.ucc.server.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventApplyerPrimaryKey implements Serializable {

    private long userId;

    private long eventId;

    @Column(nullable = false, columnDefinition = "bool default false")
    private boolean paid;

    @Column(length = 1024)
    private String messageBoard;

    public EventApplyerPrimaryKey(long userId, long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public EventApplyerPrimaryKey() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventApplyerPrimaryKey that = (EventApplyerPrimaryKey) o;
        return getUserId() == that.getUserId() &&
                getEventId() == that.getEventId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEventId());
    }
}
