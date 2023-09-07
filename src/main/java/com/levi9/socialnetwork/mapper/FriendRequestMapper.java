package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.FriendRequestEntity;
import com.levi9.socialnetwork.response.FriendRequestResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FriendRequestMapper {

    private final UserMapper userMapper;

    public FriendRequestResponse toFriendRequestResponse(FriendRequestEntity friendRequest) {
        return FriendRequestResponse.builder()
                .friendRequestId(friendRequest.getId())
                .sender(userMapper.mapUserEntityToRegisterUserResponse(friendRequest.getSender()))
                .build();
    }
}
