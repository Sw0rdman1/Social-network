package com.levi9.socialnetwork.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserResponse extends UserResponse {
    private String id;
    private Boolean active;

    @Builder
    public RegisterUserResponse(String id, String username, String email, Boolean active) {
        super(username, email);
        this.id = id;
        this.active = active;
    }
}
