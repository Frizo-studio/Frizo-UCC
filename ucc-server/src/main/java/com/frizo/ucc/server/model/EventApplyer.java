package com.frizo.ucc.server.model;

import com.frizo.ucc.server.model.audit.UserDateAudit;

import javax.persistence.*;

@Entity
@Table(name = "event_applyers")
public class EventApplyer extends UserDateAudit {

    @EmbeddedId
    private EventApplyerPrimaryKey eventApplyerPrimaryKey;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @MapsId("eventId")
    @ManyToOne
    @JoinColumn(name="eventId")
    private Event event;

    public EventApplyerPrimaryKey getEventApplyerPrimaryKey() {
        return eventApplyerPrimaryKey;
    }

    public void setEventApplyerPrimaryKey(EventApplyerPrimaryKey eventApplyerPrimaryKey) {
        this.eventApplyerPrimaryKey = eventApplyerPrimaryKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
