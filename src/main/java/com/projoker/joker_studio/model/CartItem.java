package com.projoker.joker_studio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    //Here i made big mistake. I can able to void this jasonIgnore
    // request here via of proper handling of cartdto, BUt in my business logic i didn't done it
    private Cart cart;

    public void UpdateTotalPrice(){
        this.totalPrice=this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
