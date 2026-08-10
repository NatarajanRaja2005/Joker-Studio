package com.projoker.joker_studio.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddEventAccessoriesRequest{
    private String name;
    private String description;
    //prices
    private BigDecimal wedding;
    private BigDecimal collegeFestivals;
    private BigDecimal others;
}
