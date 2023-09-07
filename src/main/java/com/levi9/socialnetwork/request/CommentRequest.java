package com.levi9.socialnetwork.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(@NotBlank String text) { }
