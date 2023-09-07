package com.levi9.socialnetwork.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostModificationRequest {
    private Long id;
    private String text;
    private boolean closed;
}
