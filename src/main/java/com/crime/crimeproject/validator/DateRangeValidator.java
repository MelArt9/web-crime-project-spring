package com.crime.crimeproject.validator;

import com.crime.crimeproject.annotation.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<DateRange, Date> {

    @Override
    public void initialize(DateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        if (date == null) {
            return false;
        }

        LocalDate dateCrime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.now();

        return !dateCrime.isBefore(minDate) && !dateCrime.isAfter(maxDate);
    }
}
