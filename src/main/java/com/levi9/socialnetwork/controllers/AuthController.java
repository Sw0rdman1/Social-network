package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.request.LoginUserRequest;
import com.levi9.socialnetwork.request.RegisterUserRequest;
import com.levi9.socialnetwork.request.ResetPasswordRequest;
import com.levi9.socialnetwork.request.VerifyEmailRequest;
import com.levi9.socialnetwork.response.UserResponse;
import com.levi9.socialnetwork.service.auth.AuthService;
import com.levi9.socialnetwork.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth/users")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        UserResponse user = authService.registerUser(registerUserRequest.getUsername(), registerUserRequest.getPassword(),
                registerUserRequest.getEmail());
        return new ResponseEntity<>(user, CREATED);
    }


    @PostMapping(value = "/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        String token = authService.getAccessToken(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        UserResponse loggedInUserResponse = authService.returnLoggedInUser(loginUserRequest.getUsername(), token);
        return new ResponseEntity<>(loggedInUserResponse, OK);
    }


}