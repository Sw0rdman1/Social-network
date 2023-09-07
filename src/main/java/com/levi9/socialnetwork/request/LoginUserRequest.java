package com.levi9.socialnetwork.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    private String username;
    private String password;
}
