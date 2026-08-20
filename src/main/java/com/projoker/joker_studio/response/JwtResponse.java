package com.projoker.joker_studio.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private long id;
    private String token;
}
