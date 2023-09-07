package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class HiddenPostRequest {

    private Long postId;

    @NotEmpty(message = "List of friends cannot be empty")
    private List<String> usernames;
}