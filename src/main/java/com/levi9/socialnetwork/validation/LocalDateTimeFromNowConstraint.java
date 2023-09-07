package com.levi9.socialnetwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

@Documented
@Constraint(validatedBy = LocalDateTimeFromNowValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimeFromNowConstraint {

    String message() default "Date and time must be after the current moment by at least {value} {unit}";

    int value() default 48;

    ChronoUnit unit() default ChronoUnit.HOURS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}