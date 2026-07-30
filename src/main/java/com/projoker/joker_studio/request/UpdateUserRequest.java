package com.projoker.joker_studio.request;

import lombok.Data;

@Data
public class UpdateUserRequest{
private String firstName;
private String lastName;
private Long phone;
private String email;
}
