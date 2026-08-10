package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.model.AgendaDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class AgendaDto {
    private Long id;
    private List<AgendaDetails> accessoriesSet=new ArrayList<>();
    private BigDecimal totalPrice;
}
