package com.levi9.socialnetwork.service.friendRequest;

import com.levi9.socialnetwork.entity.FriendRequestEntity;
import com.levi9.socialnetwork.entity.FriendshipEntity;
import com.levi9.socialnetwork.entity.RequestStatusEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.FriendRequestException;
import com.levi9.socialnetwork.mapper.FriendRequestMapper;
import com.levi9.socialnetwork.repository.FriendRequestRepository;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.response.FriendRequestResponse;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestMapper friendRequestMapper;


    /**
     * Akoo kroisnik kome je stigao zahtev psaolje korisniku od koga je zahtev automatski ce se prihvatiti i postace prjatelji
     *
     * @param receiverUsername Korisničko ime korisnika koji će primiti zahtev za prijateljstvo.
     * @return String koji potvrdjuje da je zahtevv uspesno poslat
     *
     * @throws EntityNotFoundException ako korsnik koji salje ili kojem je zahtev poslat ne postoji
     * @throws  FriendRequestException ako je korisnik sam sebi poslao zahtev ili zahtev vec postoji
     */
    @Override
    public String createFriendRequest(String receiverUsername) {

        String senderUsername = AuthUtil.getPrincipalUsername();

        UserEntity sender = userRepository
                .findByUsername(senderUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, senderUsername)));
        UserEntity receiver = userRepository
                .findByUsername(receiverUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, receiverUsername)));

        if (isSelfFriendRequest(sender.getUsername(), receiver.getUsername())) {
            throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_SELF_FRIEND_REQUEST);
        }

        Optional<FriendshipEntity> friendship = friendshipRepository.findByPairOfIds(sender.getId(), receiver.getId());
        if (friendship.isPresent()) {
            throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_ALREADY_FRIENDS);
        }

        Optional<FriendRequestEntity> friendRequest = friendRequestRepository.findByPairOfIds(sender.getId(), receiver.getId());
        Optional<FriendRequestEntity> reverseFriendRequest = friendRequestRepository.findPendingFriendRequest(receiver.getId(), sender.getId());

        if (reverseFriendRequest.isPresent()) {
            acceptFriendRequest(receiverUsername);
        } else if (friendRequest.isPresent()) {
            if (friendRequest.get().getStatus().equals(RequestStatusEntity.PENDING)) {
                throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_ALREADY_PENDING_REQUEST);
            } else if (friendRequest.get().getRequestCounter() >= 3) {
                throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_REQUEST_TIMES_LIMIT);
            } else {
                friendRequest.get().setStatus(RequestStatusEntity.PENDING);
                friendRequest.get().setRequestCounter(friendRequest.get().getRequestCounter() + 1);
                friendRequestRepository.save(friendRequest.get());
            }
        } else {
            FriendRequestEntity newFriendsRequest = new FriendRequestEntity();
            newFriendsRequest.setReceiver(receiver);
            newFriendsRequest.setStatus(RequestStatusEntity.PENDING);
            newFriendsRequest.setSender(sender);
            newFriendsRequest.setRequestCounter(1);
            friendRequestRepository.save(newFriendsRequest);
        }

        return GenericMessages.SUCCESS_MESSAGE_REQUEST_SENT;
    }

    @Override
    public List<FriendRequestResponse> getPendingFriendRequests() {
        return friendRequestRepository.findByReceiverIdAndStatus(AuthUtil.getPrincipalId(), RequestStatusEntity.PENDING)
                .stream()
                .map(friendRequestMapper::toFriendRequestResponse)
                .toList();
    }


    /**
     * @throws EntityNotFoundException ako korisnik koji salje ili prima zahtev ne postoji
     * @throws FriendRequestException ako zahtev za prijateljstvo ne postoji
     *
     * @param senderUsername Korisničko ime korisnika koji je poslao zahtev za prijateljstvo.
     * @return String sa porukom o uspesnom prihvatanju zahteva
     */
    @Override
    public String acceptFriendRequest(String senderUsername) {

        String receiverUsername = AuthUtil.getPrincipalUsername();
        UserEntity receiver = userRepository
                .findByUsername(receiverUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, receiverUsername)));
        UserEntity sender = userRepository
                .findByUsername(senderUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, senderUsername)));

        FriendRequestEntity pendingFriendRequest = friendRequestRepository
                .findPendingFriendRequest(sender.getId(), receiver.getId())
                .orElseThrow(() -> new FriendRequestException(GenericMessages.ERROR_MESSAGE_REQUEST_DOES_NOT_EXIST));

        friendRequestRepository.delete(pendingFriendRequest);

        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setUser1(sender);
        friendship.setUser2(receiver);
        friendship.setDateConnected(LocalDateTime.now());
        friendshipRepository.save(friendship);

        return GenericMessages.SUCCESS_MESSAGE_REQUEST_ACCEPTED;
    }


    /**
     * @throws EntityNotFoundException ako korisnik koji salje ili prima zahtev ne postoji
     * @throws FriendRequestException ako zahtev za prijateljstvo ne postoji
     *
     * @param senderUsername Korisničko ime korisnika koji je poslao zahtev za prijateljstvo.
     * @return String sa porukom o uspesnom odbijanju zahteva
     */
    @Override
    public String declineFriendRequest(String senderUsername) {

        String receiverUsername = AuthUtil.getPrincipalUsername();
        UserEntity receiver = userRepository
                .findByUsername(receiverUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, receiverUsername)));
        UserEntity sender = userRepository
                .findByUsername(senderUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, senderUsername)));

        FriendRequestEntity friendRequest = friendRequestRepository
                .findPendingFriendRequest(sender.getId(), receiver.getId())
                .orElseThrow(() -> new FriendRequestException(GenericMessages.ERROR_MESSAGE_REQUEST_DOES_NOT_EXIST));

        friendRequest.setStatus(RequestStatusEntity.REJECTED);
        friendRequestRepository.save(friendRequest);

        return GenericMessages.SUCCESS_MESSAGE_REQUEST_DECLINED;
    }

    /**
     *
     * @param senderUsername korisnicko ime onog ko salje zahtev
     * @param receiverUsername korisnicko ime onog ko prima zahtev
     * @return boolean vrednost da li je zahtev poslat samom sebi
     */
    private boolean isSelfFriendRequest(String senderUsername, String receiverUsername) {
        return senderUsername.equals(receiverUsername);
    }
}