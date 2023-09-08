package com.levi9.socialnetwork.service.user;

import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.mapper.UserMapper;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.response.UserResponse;
import com.levi9.socialnetwork.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserMapper userMapper;


    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElse(null);
    }

    @Override
    public UserEntity findById(String id) {
        return userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(
                        "User with ID: " + id + " not found"));
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new EntityNotFoundException(
                        "User with username: " + username + " not found"));
    }

    @Override
    public List<UserResponse> findUsersByUsernameContaining(String searchCriteria) {
        UserEntity currentUser = findById(AuthUtil.getPrincipalId());
        List<String> currentUserFriends = friendshipRepository.findUserFriendsEmails(currentUser.getId());
        List<UserEntity> responseList = searchCriteria == null ?
                userRepository.findAllByActiveIsTrueAndUsernameIsNot(currentUser.getUsername()) :
                userRepository.findAllByUsernameContainingIgnoreCaseAndActiveIsTrueAndUsernameIsNot(searchCriteria, currentUser.getUsername());

        return responseList.stream().map(user -> {
            List<String> mutualFriendCount = findMutualFriends(currentUserFriends, user.getId());
            return userMapper.mapUserEntityToSearchUserResponse(user, mutualFriendCount.size(), currentUserFriends.contains(user.getEmail()));
        }).toList();
    }

    @Override
    public List<String> findMutualFriends(List<String> currentUserFriends, String otherUserID) {
        List<String> response = friendshipRepository.findUserFriendsEmails(otherUserID);
        response.retainAll(currentUserFriends);
        return response;
    }
}
