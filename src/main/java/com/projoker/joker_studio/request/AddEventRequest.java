package com.projoker.joker_studio.request;

import com.projoker.joker_studio.enums.EventName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AddEventRequest {
    private String eventName;
    private String description;
    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;
    private Long agendaId;
    private Long userId;
}
