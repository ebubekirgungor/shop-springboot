package com.ebubekirgungor.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.service.UserService;
import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.response.UserResponse;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(currentUser.getEmail());
        userResponse.setFirst_name(currentUser.getFirst_name());
        userResponse.setLast_name(currentUser.getLast_name());
        userResponse.setPhone(currentUser.getPhone());
        userResponse.setBirth_date(currentUser.getBirth_date());
        userResponse.setGender(currentUser.getGender());
        userResponse.setCart(currentUser.getCart());

        return ResponseEntity.ok(userResponse);
    }
}
