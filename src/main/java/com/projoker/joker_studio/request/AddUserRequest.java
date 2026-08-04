package com.projoker.joker_studio.request;

import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.OrderAddress;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class AddUserRequest {
    private String firstName;
    private String lastName;
    private Long phone;
    private String email;
    private String password;
    private OrderAddress Address;
}
