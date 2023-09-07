package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.response.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostResponse toPostResponse(PostEntity post) {
        return createPostResponse(post);
    }

    private PostResponse createPostResponse(PostEntity post) {
        return PostResponse.builder()
                .id(post.getId())
                .text(post.getText())
                .dateTimeCreated(post.getDateTimeCreated())
                .closed(post.isClosed())
                .group(post.getGroup())
                .creator(post.getCreator())
                .build();
    }
}