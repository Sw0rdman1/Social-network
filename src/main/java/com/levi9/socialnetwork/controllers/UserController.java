package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.response.UserResponse;
import com.levi9.socialnetwork.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping(value = {"/{searchCriteria}", "/"})
    public ResponseEntity<List<UserResponse>> searchUsers(@PathVariable(required = false) String searchCriteria) {
        List<UserResponse> foundUsers = userService.findUsersByUsernameContaining(searchCriteria);
        return new ResponseEntity<>(foundUsers, OK);
    }
}
