package com.levi9.socialnetwork.mapper;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.response.RegisterUserResponse;
import com.levi9.socialnetwork.response.SearchUserResponse;
import com.levi9.socialnetwork.response.UserResponse;
import org.springframework.stereotype.Component;
@Component
public class UserMapper {
    public UserResponse mapUserEntityToRegisterUserResponse(UserEntity user) {
        return RegisterUserResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .active(user.getActive())
                    .build();
        }
        public UserResponse mapUserEntityToSearchUserResponse(UserEntity user, Integer mutualFriends, boolean isFriend) {
            return SearchUserResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .isFriend(isFriend)
                    .mutualFriendNumber(mutualFriends)
                    .build();
        }


    }