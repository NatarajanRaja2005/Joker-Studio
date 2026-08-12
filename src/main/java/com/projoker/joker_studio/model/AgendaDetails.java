package com.projoker.joker_studio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projoker.joker_studio.enums.EventName;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendaDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_accessories_id")
    private EventAccessories eventAccessories;
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(name="event_name")
    private EventName eventName;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    @JsonIgnore
    private Agenda agenda;


}
