package com.frizo.ucc.server.payload.request;

public class UpdateEventRequest extends CreateEventRequest {
    private Long eventId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
