package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.enums.OrderStatus;
import com.projoker.joker_studio.model.OrderAddress;
import com.projoker.joker_studio.model.OrderDetails;
import com.projoker.joker_studio.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private LocalDateTime orderDateTime;
    private BigDecimal orderAmount;
    private Set<OrderDetails> orderDetails=new HashSet<>();
    private String instruction;
    private OrderAddress orderAddress;
    private OrderStatus orderStatus;
    private UserDto userDto;
}
