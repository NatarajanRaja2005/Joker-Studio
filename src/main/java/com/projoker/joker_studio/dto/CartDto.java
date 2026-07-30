package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.model.CartItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private Set<CartItem> itemList;
    private BigDecimal totalAmount;
}
