package com.projoker.joker_studio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Long phone;
    private String email;
    private String password;
    private boolean emailVerification;
    private boolean deactivate;
    @Embedded
    private OrderAddress Address;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL,orphanRemoval = true)
    private Cart cart;

}
