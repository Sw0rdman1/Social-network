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


    /**
     *
     * @param user Entitet korisnika koji se čuva ili ažurira.
     * @return sacuvani korisnik
     */
    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElse(null);
    }

    /**
     * @throws EntityNotFoundException ako korisnik ne postoji
     *
     * @param id ID korisnika.
     * @return korisnik kome odgovara uneti ID
     */
    @Override
    public UserEntity findById(String id) {
        return userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(
                        "User with ID: " + id + " not found"));
    }

    /**
     * @throws EntityNotFoundException ako korisnik ne postoji
     *
     * @param username Korisničko ime korisnika.
     * @return korisnik kome odgovara uneto korisnicko ime
     */
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new EntityNotFoundException(
                        "User with username: " + username + " not found"));
    }

    /**
     *
     * @param searchCriteria Kriterijum pretrage za korisnička imena.
     * @return list korisnika cije korisnicko ime pocinje sa unetim karakterima
     */
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

    /**
     * @param currentUserFriends Lista ID-eva prijatelja trenutnog korisnika.
     * @param otherUserID       ID drugog korisnika.
     * @return listu ID-ijeva svih zajednickih prijatelja ulogovanog korisnika i korisnika ciji ID prosledjujemo
     */
    @Override
    public List<String> findMutualFriends(List<String> currentUserFriends, String otherUserID) {
        List<String> response = friendshipRepository.findUserFriendsEmails(otherUserID);
        response.retainAll(currentUserFriends);
        return response;
    }
}
