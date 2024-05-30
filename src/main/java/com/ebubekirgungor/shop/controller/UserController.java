package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.ebubekirgungor.shop.response.UserResponse;

import java.util.List;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
