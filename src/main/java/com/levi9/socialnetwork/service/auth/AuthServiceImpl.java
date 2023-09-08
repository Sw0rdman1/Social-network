package com.levi9.socialnetwork.service.auth;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.levi9.socialnetwork.config.JwtService;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.UserRegistrationException;
import com.levi9.socialnetwork.mapper.UserMapper;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.response.LoggedInUserResponse;
import com.levi9.socialnetwork.response.UserResponse;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.GenericMessages;
import com.levi9.socialnetwork.util.RandomIDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    /**
     *
     * @throws UserRegistrationException ako vec postoji user sa istim emailom
     */
    @Override
    public UserResponse registerUser(String username, String password, String email) {
        UserEntity user = userService.findByEmail(email);

        if (isNotEmpty(user)) {
            throw new UserRegistrationException(String.format(GenericMessages.EMAIL_ALREADY_EXIST_MESSAGE, user.getEmail()));
        } else {
            UserEntity newUser = UserEntity.builder()
                    .id(RandomIDGenerator.generateRandomString())
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .active(true)
                    .build();

            userRepository.save(newUser);
            return userMapper.mapUserEntityToRegisterUserResponse(newUser);
        }
    }

    @Override
    public String getAccessToken(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );


        var user = userRepository.findByUsername(username)
                .orElseThrow();

        return jwtService.generateToken(user);
    }


    @Override
    public UserResponse returnLoggedInUser(String email, String token) {
        return LoggedInUserResponse
                        .builder()
                        .email(email)
                        .token(token)
                        .build();
    }
}
