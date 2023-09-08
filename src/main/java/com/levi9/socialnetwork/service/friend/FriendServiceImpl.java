package com.levi9.socialnetwork.service.friend;

import com.levi9.socialnetwork.entity.FriendshipEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.levi9.socialnetwork.util.GenericMessages;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {

    private UserRepository userRepository;

    private FriendshipRepository friendshipRepository;

    @Override
    public void remove(String userToBeDeleted) {

        String loggedUser = AuthUtil.getPrincipalId();

        if (loggedUser.isEmpty()) {
            throw new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN);
        }

        UserEntity receiver = userRepository.findByUsername(userToBeDeleted)
                .orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_INVALID_FRIEND));

        Optional<FriendshipEntity> friendshipToDelete = friendshipRepository.findByPairOfIds(loggedUser, receiver.getId());

        if (friendshipToDelete.isEmpty()) {
            throw new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_FRIENDSHIP_DOES_NOT_EXIST, receiver.getId()));
        }

        friendshipRepository.delete(friendshipToDelete.get());
    }
}