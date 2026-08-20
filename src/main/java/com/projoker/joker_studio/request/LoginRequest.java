package com.projoker.joker_studio.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
