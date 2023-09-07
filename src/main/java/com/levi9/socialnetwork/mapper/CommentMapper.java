package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.CommentEntity;
import com.levi9.socialnetwork.response.CommentResponse;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {
    CommentResponse mapCommentEntityToCommentResponse(CommentEntity comment);
}
