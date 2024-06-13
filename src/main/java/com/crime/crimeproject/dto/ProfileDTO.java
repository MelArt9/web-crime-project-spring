package com.crime.crimeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProfileDTO {

    private UUID id;
    private String login;
    private String password;
    private String email;

}
