package com.projoker.joker_studio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class StudioConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
