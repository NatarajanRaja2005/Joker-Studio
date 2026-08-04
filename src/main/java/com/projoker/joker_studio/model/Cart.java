package com.projoker.joker_studio.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> itemList;

    private BigDecimal totalAmount;

    @OneToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

}
