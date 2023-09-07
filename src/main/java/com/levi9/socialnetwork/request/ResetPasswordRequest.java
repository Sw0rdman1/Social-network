package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    //Password must contain at least 8 characters, 1 Uppercase,1 lowercase,  number and one 1 special character
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String newPassword;

    @Size(min = 6, max = 6)
    private String confirmationCode;

    @NotBlank(message = "Username cannot be empty!")
    private String username;
}