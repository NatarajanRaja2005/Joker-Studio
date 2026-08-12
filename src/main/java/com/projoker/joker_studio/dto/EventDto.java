package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.enums.EventStatus;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventDto {
    private Long id;
    private EventName eventName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String description;
    private UserDto user;
    private AgendaDto agenda;
    private EventStatus eventStatus;
    private BigDecimal advance;
}
