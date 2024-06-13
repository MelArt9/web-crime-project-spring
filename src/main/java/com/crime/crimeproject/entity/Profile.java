package com.crime.crimeproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Profile {

    private UUID id;
    private String login;
    private String password;
    private String email;

}
