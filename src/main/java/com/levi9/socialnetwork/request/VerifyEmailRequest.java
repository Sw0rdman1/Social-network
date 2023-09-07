package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailRequest {

    @Size(min = 6, max = 6)
    private String verificationCode;

    @NotBlank(message = "Username cannot be empty!")
    private String username;
}
