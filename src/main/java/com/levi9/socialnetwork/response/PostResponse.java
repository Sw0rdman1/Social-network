package com.levi9.socialnetwork.response;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String text;
    private LocalDateTime dateTimeCreated;
    private boolean closed;
    private GroupEntity group;
    private UserEntity creator;
}