package com.levi9.socialnetwork.response;

import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentResponse {
    private String text;
    private PostEntity post;
    private UserEntity creator;
    private LocalDateTime dateTimeCreated;
}
