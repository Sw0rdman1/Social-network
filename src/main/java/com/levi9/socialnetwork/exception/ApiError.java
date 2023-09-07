package com.levi9.socialnetwork.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<ApiValidationError> subErrors;

    public ApiError(HttpStatus status, String message) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    private void addSubError(ApiValidationError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(fieldError ->
                addSubError(new ApiValidationError(fieldError.getObjectName(),
                        fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage())));
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(objectError ->
                addSubError(new ApiValidationError(objectError.getObjectName(), objectError.getDefaultMessage())));
    }
}
