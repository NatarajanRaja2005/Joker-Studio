package com.projoker.joker_studio.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotifyMessage {
    private String subject;
    private String message;
}
