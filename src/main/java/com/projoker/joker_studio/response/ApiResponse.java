package com.projoker.joker_studio.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}
