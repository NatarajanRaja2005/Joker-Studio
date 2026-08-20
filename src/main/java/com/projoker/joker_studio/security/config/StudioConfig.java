package com.projoker.joker_studio.security.config;

import com.projoker.joker_studio.security.jwt.JwtAuthFilter;
import com.projoker.joker_studio.security.user_details.StudioUserDetails;
import com.projoker.joker_studio.security.user_details.StudioUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class StudioConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final StudioUserDetailsService userDetailsService;
    private static final List<String> Secured_Url=List.of(
            "/api/v1/carts/**",
            "/api/v1/products/get/**",
            "/api/v1/cartItems/**",
            "/api/v1/user/add/**",
            "/api/v1/portfolio/get/**",
            "/api/v1/portfolio-media/**",
            "/api/v1/user/verify/**",
            "/api/v1/agenda/get/**",
            "/api/v1/accessories/get/**",
            "/api/v1/auth/login/**");

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider  daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(Secured_Url.toArray(String[] :: new)).permitAll()
                                .anyRequest().authenticated())
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
