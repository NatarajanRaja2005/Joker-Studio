package com.projoker.joker_studio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private String eventDescription;
    private LocalDate createdDate;

    @OneToOne
    private Agenda agenda;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy="portfolio")
    private List<PortfolioMedia> portfolioMedia=new ArrayList<>();
}
