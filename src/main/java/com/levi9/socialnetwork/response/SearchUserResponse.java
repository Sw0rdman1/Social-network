package com.levi9.socialnetwork.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchUserResponse extends UserResponse {
    private Integer mutualFriendNumber;
    private boolean isFriend;

    @Builder
    public SearchUserResponse(String username, String email, Integer mutualFriendNumber, boolean isFriend) {
        super(username, email);
        this.mutualFriendNumber = mutualFriendNumber;
        this.isFriend = isFriend;
    }
}
