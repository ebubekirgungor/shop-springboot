package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.UpdateUserDto;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.ebubekirgungor.shop.response.UserResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${origin-url}", allowCredentials = "true")
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

    @PutMapping("/me")
    public ResponseEntity<Map<String, String>> updateAuthenticatedUser(@RequestBody UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        currentUser.setFirst_name(updateUserDto.getFirst_name());
        currentUser.setLast_name(updateUserDto.getLast_name());
        currentUser.setPhone(updateUserDto.getPhone());
        currentUser.setBirth_date(updateUserDto.getBirth_date());
        currentUser.setGender(updateUserDto.getGender());

        userRepository.save(currentUser);

        Map<String, String> updateResponse = new HashMap<>();
        updateResponse.put("status", "ok");
        return ResponseEntity.ok(updateResponse);
    }
}
