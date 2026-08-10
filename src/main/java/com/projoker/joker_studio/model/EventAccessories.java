package com.projoker.joker_studio.model;


import com.projoker.joker_studio.enums.EventName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EventAccessories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private HashMap<EventName,BigDecimal> price=new HashMap<>();
}
