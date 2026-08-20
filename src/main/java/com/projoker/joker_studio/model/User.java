package com.projoker.joker_studio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.util.Collection;
import java.util.HashSet;

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
    private boolean phoneVerification;
    private boolean deactivate;

    @ManyToMany(cascade =
            {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH},
    fetch = FetchType.EAGER)
    @JoinTable(name="user_Roles",joinColumns = @JoinColumn(name="user_Id",referencedColumnName = "id"),
           inverseJoinColumns = @JoinColumn(name="role_Id",referencedColumnName = "id"))
    private Collection<Role> roles=new HashSet<>();

    @Embedded
    private OrderAddress Address;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL,orphanRemoval = true)
    private Cart cart;

}
