package com.levi9.socialnetwork.request;

import com.levi9.socialnetwork.validation.LocalDateTimeFromNowConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
public class EventRequest {

    @NotBlank(message = "You must provide location for the event")
    private String location;

    @NotNull
    @LocalDateTimeFromNowConstraint(value = 48, unit = ChronoUnit.HOURS)
    private LocalDateTime dateTime;
}