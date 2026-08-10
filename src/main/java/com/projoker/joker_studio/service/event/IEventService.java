package com.projoker.joker_studio.service.event;

import com.projoker.joker_studio.model.Event;

public interface IEventService {
    Event BookEvent();
    String cancelEvent(Long eventId);
    Event updateEvent(Long eventId);
}
