package com.projoker.joker_studio.service.event;

import com.projoker.joker_studio.dto.EventDto;
import com.projoker.joker_studio.model.Event;
import com.projoker.joker_studio.request.AddEventRequest;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface IEventService {
    Event BookEvent(AddEventRequest request);
    String cancelEvent(Long eventId);
    Event updateEvent(Long eventId,AddEventRequest request);

    Event getEventByIdForUser(Long userId,Long eventId);
    Event getEventByIdForAdmin(Long eventId);

    List<Event> getEventsByUserId(Long userId);

    List<Event> getEventsByDateForAdmin(String date);
    List<Event> getEventsByDateForUser(Long userId,String date);

    List<Event> getUpComingEventsForAdmin();
    List<Event> getUpComingEventsForUser(Long userId);

    List<Event> getPendingEventsForAdmin();
    List<Event> getPendingEventsForUser(Long userId);

    List<Event> getBookedEventsForAdmin();
    List<Event> getBookedEventsForUser(Long userId);

    List<Event> getCancelledEventsForAdmin();
    List<Event> getCancelledEventsForUser(Long userId);

    EventDto eventDto(Event event);

    Event changeEventStatus(Long eventId, String status);
}
