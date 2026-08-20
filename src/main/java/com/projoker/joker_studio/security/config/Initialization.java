package com.projoker.joker_studio.security.config;

import com.projoker.joker_studio.model.Role;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.RoleRepository;
import com.projoker.joker_studio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class Initialization {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Bean
    public String initializeUser(){
        String username="deepigaraja4903@gmail.com";
        User user=userRepository.findByEmail(username);
        if(user!=null){
            return " ";
        }
        else{
            user=new User();
        }
        user.setEmail("deepigaraja4903@gmail.com");
        user.setEmailVerification(true);
        Collection<Role> roles=new HashSet<>();
        roles.add(roleRepository.findByName("ADMIN"));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode("12345"));
        user.setLastName("Raja");
        user.setFirstName("Deepi Joker");

        userRepository.save(user);
        return "User Initialized";
    }
}
