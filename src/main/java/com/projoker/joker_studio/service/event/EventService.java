package com.projoker.joker_studio.service.event;

import com.projoker.joker_studio.dto.AgendaDto;
import com.projoker.joker_studio.dto.EventDto;
import com.projoker.joker_studio.dto.UserDto;
import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.enums.EventStatus;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.exception.MoreEventExistsException;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.Event;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.EventRepository;
import com.projoker.joker_studio.request.AddEventRequest;
import com.projoker.joker_studio.service.agenda.IAgendaService;
import com.projoker.joker_studio.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService{
    private final EventRepository eventRepository;
    private final IAgendaService agendaService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Event BookEvent(AddEventRequest request) {
        Event event=new Event();
        event=settingEvent(request,event);
        //Here I'm implementing restriction for users to does not
        // allow to create of more than of 3 Non booked request
        List<Event> pendingEventsForUser=getPendingEventsForUser(event.getUser().getId());

        if(pendingEventsForUser.size()>=3){
            throw new MoreEventExistsException("More non booked events are exists. Kindly wait for response.");
        }

        return eventRepository.save(event);
    }

    private Event settingEvent(AddEventRequest request,Event event){
        EventName eventName=EventName.valueOf(request.getEventName().toUpperCase());
        Agenda agenda=agendaService.getAgendaById(request.getAgendaId());
        User user=userService.getUser(request.getUserId());

        if(agenda==null){
            throw new ItemNotExistException("Agenda Not Found. Kindly ensure of valid agenda.");
        }
        event.setEventName(eventName);
        event.setEventStatus(EventStatus.NOT_BOOKED);
        event.setAdvance(BigDecimal.ZERO);
        event.setAgenda(agenda);
        event.setDescription(request.getDescription());
        event.setUser(user);

        event.setFromDate(LocalDate.parse(request.getFromDate()));
        event.setToDate(LocalDate.parse(request.getToDate()));
        event.setToTime(LocalTime.parse(request.getToTime()));
        event.setFromTime(LocalTime.parse(request.getFromTime()));

        if(event.getToDate().isBefore(event.getFromDate())){
            throw new IllegalArgumentException(
                    "To date cannot be before from date."
            );
        }

        return event;
    }

    @Override
    public String cancelEvent(Long eventId) {
        Event event=getEventByIdForAdmin(eventId);
        event.setEventStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
        return "Event Cancelled Successfully!";
    }

    @Override
    public Event updateEvent(Long eventId, AddEventRequest request) {
        Event event=getEventByIdForAdmin(eventId);
        event=settingEvent(request,event);
        return eventRepository.save(event);
    }

    @Override
    public Event getEventByIdForUser(Long userId, Long eventId) {
        Optional<Event> event=eventRepository.findByIdAndUserId(userId,eventId);
        if(event.isEmpty()){
            throw new ItemNotExistException("Event not exists.");
        }
        return event.get();
    }

    @Override
    public Event getEventByIdForAdmin(Long eventId) {
        Optional<Event> event=eventRepository.findById(eventId);
        if(event.isEmpty()){
            throw new ItemNotExistException("Event not exists.");
        }
        return event.get();
    }

    @Override
    public List<Event> getEventsByUserId(Long userId) {
        List<Event> events=eventRepository.findAllByUserId(userId);
        if(events.isEmpty()){
            throw new ItemNotExistException("Events Not Found.");
        }
        return events;
    }

    @Override
    public List<Event> getEventsByDateForAdmin(String date) {
        LocalDate fromdate= Date.valueOf(date).toLocalDate();
        List<Event> events=eventRepository.findAllByFromDateBetween(fromdate,fromdate.plusDays(2));
        if(events.isEmpty()){
            throw new ItemNotExistException("Events Not Found.");
        }
        return events;
    }

    @Override
    public List<Event> getEventsByDateForUser(Long userId, String date) {
        LocalDate fromdate= Date.valueOf(date).toLocalDate();
        List<Event> events=eventRepository.findAllByFromDateBetweenAndUserId(fromdate,fromdate.plusDays(2),userId);
        if(events.isEmpty()){
            throw new ItemNotExistException("Events Not Found.");
        }
        return events;
    }


    @Override
    public List<Event> getUpComingEventsForAdmin() {
        LocalDate fromdate= LocalDate.now();
        List<Event> events=eventRepository.getLatestUpcomingEvents();
        if(events.isEmpty()){
            throw new ItemNotExistException("Events Not Found.");
        }
        return events;
    }

    //Should have to modify the to date
    @Override
    public List<Event> getUpComingEventsForUser(Long userId) {
        LocalDate fromdate= LocalDate.now();
        List<Event> events=eventRepository.getLastestUpComingEventsForUser(userId);
        if(events.isEmpty()){
            throw new ItemNotExistException("Events Not Found.");
        }
        return events;
    }

    @Override
    public List<Event> getPendingEventsForAdmin() {
        return eventRepository.findAllByEventStatus(EventStatus.BOOKED);
    }

    @Override
    public List<Event> getPendingEventsForUser(Long userId) {
        return eventRepository.findAllByEventStatusAndUser_Id(EventStatus.BOOKED,userId);
    }

    @Override
    public List<Event> getBookedEventsForAdmin() {
        return eventRepository.findAllByEventStatus(EventStatus.BOOKED);
    }

    @Override
    public List<Event> getBookedEventsForUser(Long userId) {
        return eventRepository.findAllByEventStatusAndUser_Id(EventStatus.BOOKED,userId);
    }

    @Override
    public List<Event> getCancelledEventsForAdmin() {
        return eventRepository.findAllByEventStatus(EventStatus.CANCELLED);
    }

    @Override
    public List<Event> getCancelledEventsForUser(Long userId) {
        return eventRepository.findAllByEventStatusAndUser_Id(EventStatus.CANCELLED,userId);
    }

    @Override
    public EventDto eventDto(Event event){
        EventDto eventDto=new EventDto();
        User user= userService.getUser(event.getUser().getId());
        Agenda agenda=agendaService.getAgendaById(event.getAgenda().getId());
        UserDto userDto=modelMapper.map(user, UserDto.class);
        AgendaDto agendaDto=modelMapper.map(agenda, AgendaDto.class);

        eventDto.setId(event.getId());
        eventDto.setAdvance(event.getAdvance());
        eventDto.setDescription(event.getDescription());
        eventDto.setEventName(event.getEventName());
        eventDto.setEventStatus(event.getEventStatus());
        eventDto.setAgenda(agendaDto);
        eventDto.setUser(userDto);
        eventDto.setFromDate(event.getFromDate());
        eventDto.setFromTime(event.getFromTime());
        eventDto.setToDate(event.getToDate());
        eventDto.setToTime(event.getToTime());

        return eventDto;
    }

    @Override
    public Event changeEventStatus(Long eventId, String status){
        Event event=getEventByIdForAdmin(eventId);
        EventStatus status2=EventStatus.valueOf(status.toUpperCase());
        event.setEventStatus(status2);
        return eventRepository.save(event);
    }
}
