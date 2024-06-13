package com.crime.crimeproject.request;

import com.crime.crimeproject.annotation.DateRange;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateCrimeJournalRequest {

    @NotEmpty
    @Length(min = 4)
    private String description;

    @NotNull
    @DateRange
    private Date dateCrime;

    @NotNull
    private Boolean isClosed;

    @NotNull
    private UUID profileId;

}