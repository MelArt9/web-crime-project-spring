package com.crime.crimeproject.annotation;

import com.crime.crimeproject.validator.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME) // аннотация будет доступна через Reflection API во время выполнения программы
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface DateRange {

    String message() default "Дата преступления должна быть между '1900-01-01' и текущей датой";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; // передача метаданных в контексте валидации

}