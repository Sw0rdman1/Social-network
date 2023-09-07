package com.levi9.socialnetwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeFromNowValidator implements ConstraintValidator<LocalDateTimeFromNowConstraint, LocalDateTime> {

    private int value;
    private ChronoUnit unit;

    @Override
    public void initialize(LocalDateTimeFromNowConstraint constraintAnnotation) {
        this.value = constraintAnnotation.value();
        this.unit = constraintAnnotation.unit();
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime minDateTime = currentDateTime.plus(value, unit);

        return localDateTime.isAfter(minDateTime) || localDateTime.isEqual(minDateTime);
    }
}