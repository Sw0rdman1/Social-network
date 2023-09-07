package com.levi9.socialnetwork.response;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
public class LoggedInUserResponse extends UserResponse {
    private String token;
    @Builder
    public LoggedInUserResponse(String username, String email, String token) {
        super(username, email);
        this.token = token;
    }
}
