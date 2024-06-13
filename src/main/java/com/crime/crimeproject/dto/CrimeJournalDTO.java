package com.crime.crimeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CrimeJournalDTO {

    private UUID id;
    private String description;
    private Date dateCrime;
    private Boolean isClosed;
    private UUID profile_id;

}
