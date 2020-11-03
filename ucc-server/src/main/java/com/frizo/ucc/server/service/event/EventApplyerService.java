package com.frizo.ucc.server.service.event;

import com.frizo.ucc.server.model.EventApplyer;

import java.util.List;

public interface EventApplyerService {

    List<EventApplyer> findAllApplyers(long eventId, long ownerId);

    void applyEvent(long eventId, long applyerId);

    void confirmPaid(long eventId, long applyerId);

    void cancelPaid(long eventId, long applyerId);

    void cancelApply(long eventId, long applyerId);

    List<EventApplyer> findAllMyApplyedEvent(long eventId, long applyerId);

    void deleteAllApplyerByEventId(long eventId);
}
