package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    @Size(min = 3, max = 16, message = "Group name must be at least 3, and at most 16 characters long")
    private String groupName;

    @NotNull
    private Boolean closed;
}