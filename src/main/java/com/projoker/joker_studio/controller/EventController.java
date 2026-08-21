package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.dto.EventDto;
import com.projoker.joker_studio.model.Event;
import com.projoker.joker_studio.request.AddEventRequest;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.event.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/event")
@RequiredArgsConstructor
public class EventController {
    private final IEventService eventService;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse> bookEvent(@RequestBody AddEventRequest request){
        try {
            Event event=eventService.BookEvent(request);
            EventDto eventDto=eventService.eventDto(event);
            return ResponseEntity.ok(new ApiResponse("Event Booked Successfully!",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Booking event Failed!",e.getMessage()));
        }
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<ApiResponse> updateEvent(@PathVariable Long eventId,@RequestBody AddEventRequest request){
        try {
            Event event=eventService.updateEvent(eventId,request);
            EventDto eventDto=eventService.eventDto(event);
            return ResponseEntity.ok(new ApiResponse("Event Updated Successfully",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Booking event Failed!",e.getMessage()));
        }
    }

    @PutMapping("/cancel/{eventId}")
    public ResponseEntity<ApiResponse> cancelEvent(@PathVariable Long eventId){
        try {
            String response= eventService.cancelEvent(eventId);
            return ResponseEntity.ok(new ApiResponse("Process Done",response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Cancellation failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/id/{userId}/{eventId}")
    public ResponseEntity<ApiResponse> getEventByIdForUser(@PathVariable Long userId,@PathVariable Long eventId){
        try {
            Event event=eventService.getEventByIdForUser(userId, eventId);
            EventDto eventDto=eventService.eventDto(event);
            return ResponseEntity.ok(new ApiResponse("Retriving event is done.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/id/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getEventByIdForAdmin(@PathVariable Long eventId){
        try {
            Event event=eventService.getEventByIdForAdmin(eventId);
            EventDto eventDto=eventService.eventDto(event);
            return ResponseEntity.ok(new ApiResponse("Retriving event is done.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/by/userid/{userId}")
    public ResponseEntity<ApiResponse> getEventByUserId(@PathVariable Long userId){
        try {
            List<Event> events=eventService.getEventsByUserId(userId);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Retriving event is done.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/by/date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getEventsByDateForAdmin(@RequestParam String date){
        try {
            List<Event> events=eventService.getEventsByDateForAdmin(date);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/by/date")
    public ResponseEntity<ApiResponse> getEventsByDateForUser(@RequestParam String date,@RequestParam Long userId){
        try {
            List<Event> events=eventService.getEventsByDateForUser(userId, date);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/upcoming")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUpComingEventsForAdmin(){
        try {
            List<Event> events=eventService.getUpComingEventsForAdmin();
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/upcoming/{userId}")
    public ResponseEntity<ApiResponse> getUpComingEventsForUser(@PathVariable Long userId){
        try {
            List<Event> events=eventService.getUpComingEventsForUser(userId);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPendingEventsForAdmin(){
        try {
            List<Event> events=eventService.getPendingEventsForAdmin();
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/pending/{userId}")
    public ResponseEntity<ApiResponse> getPendingEventsForUser(@PathVariable Long userId){
        try {
            List<Event> events=eventService.getPendingEventsForUser(userId);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/booked")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getBookedEventsForAdmin(){
        try {
            List<Event> events=eventService.getBookedEventsForAdmin();
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/booked/{userId}")
    public ResponseEntity<ApiResponse> getBookedEventsForUser(@PathVariable Long userId){
        try {
            List<Event> events=eventService.getBookedEventsForUser(userId);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Retrival failed!",e.getMessage()));
        }
    }

    @GetMapping("/admin/get/cancelled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getCancelledEventsForAdmin(){
        try {
            List<Event> events=eventService.getCancelledEventsForAdmin();
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Cancellation failed!",e.getMessage()));
        }
    }

    @GetMapping("/user/get/cancelled/{userId}")
    public ResponseEntity<ApiResponse> getCancelledEventsForUser(@PathVariable Long userId){
        try {
            List<Event> events=eventService.getCancelledEventsForUser(userId);
            List<EventDto> eventDto=events.stream().map(eventService::eventDto).toList();
            return ResponseEntity.ok(new ApiResponse("Event retrived successfully.",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Event Cancellation failed!",e.getMessage()));
        }
    }

    //Only admin
    @PutMapping("/admin/status/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> changeEventStatus(@PathVariable Long eventId,@RequestParam String status){
        try {
            Event event=eventService.changeEventStatus(eventId,status);
            EventDto eventDto=eventService.eventDto(event);
            return ResponseEntity.ok(new ApiResponse("Event status changed successfully!",eventDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Changing event status is failed!",e.getMessage()));
        }
    }
}
