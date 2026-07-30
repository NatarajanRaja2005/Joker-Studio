package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.model.Cart;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long phone;
    private String email;
}
