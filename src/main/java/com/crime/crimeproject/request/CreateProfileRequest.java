package com.crime.crimeproject.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
public class CreateProfileRequest {

    @NotEmpty
    @Length(min = 3, max = 20)
    private String login;

    @NotEmpty
    @Length(min = 8, max = 50)
    private String password;

    @NotEmpty
    @Length(min = 1, max = 256)
    private String email;

}
