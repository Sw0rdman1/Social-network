package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostRequest {
    @NotBlank
    private String text;
    private boolean closed;
    private String username;
}
