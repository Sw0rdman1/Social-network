package com.levi9.socialnetwork.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private boolean closed;
    private UserResponse admin;
}