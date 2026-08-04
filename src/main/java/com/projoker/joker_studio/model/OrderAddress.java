package com.projoker.joker_studio.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigInteger;

@Embeddable
@Data
public class OrderAddress {
    private int doorNo;
    private String streetName;
    private String city;
    private String district;
    private String taluk;
    private String state;
    private BigInteger pinCode;
    private String landMark;
}
