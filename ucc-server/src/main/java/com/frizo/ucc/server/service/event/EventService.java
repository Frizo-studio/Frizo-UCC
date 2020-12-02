package com.frizo.ucc.server.service.event;

import com.frizo.ucc.server.model.Event;
import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.request.UpdateEventRequest;
import com.frizo.ucc.server.payload.response.bean.EventBean;

import java.util.List;

public interface EventService {
    EventBean getById(Long id);

    List<EventBean> findAllByQuerySpec(QueryEventRequest request);

    EventBean createEvent(Long userId, CreateEventRequest request);

    List<EventBean> findPostedEventByUserId(Long userId, int pageNumber, String sortBy, String direction);

    void deleteEvent(Long userId, Long eventId);

    EventBean updateEvent(Long userId, UpdateEventRequest request);
}
