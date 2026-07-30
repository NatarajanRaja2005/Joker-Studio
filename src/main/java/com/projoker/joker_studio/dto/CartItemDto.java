package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id;
    private Product product;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;
}
