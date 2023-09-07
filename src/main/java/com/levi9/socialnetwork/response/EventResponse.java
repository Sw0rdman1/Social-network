package com.levi9.socialnetwork.response;


import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventResponse {

    private Long id;
    private LocalDateTime dateTime;
    private String location;
    private GroupResponse group;
    private String creatorUsername;
}