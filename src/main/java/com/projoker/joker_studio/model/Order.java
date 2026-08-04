package com.projoker.joker_studio.model;

import com.projoker.joker_studio.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDateTime;
    private BigDecimal orderAmount;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderDetails> orderDetails=new HashSet<>();

    private String instruction;

    @Embedded
    private OrderAddress orderAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(){
        this.orderStatus=OrderStatus.FAILED;
    }
}
