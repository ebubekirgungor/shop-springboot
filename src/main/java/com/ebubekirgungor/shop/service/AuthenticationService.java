package com.ebubekirgungor.shop.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.LoginUserDto;
import com.ebubekirgungor.shop.model.User.RegisterUserDto;
import com.ebubekirgungor.shop.model.User.Role;
import com.ebubekirgungor.shop.model.User.UpdatePasswordDto;
import com.ebubekirgungor.shop.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setFirst_name(input.getFirst_name());
        user.setLast_name(input.getLast_name());
        user.setPhone(input.getPhone());
        user.setBirth_date(input.getBirth_date());
        user.setGender(input.getGender());
        user.setCart(new ArrayList<>());
        user.setRole(Role.CUSTOMER);

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public ResponseEntity<String> updatePassword(User user, UpdatePasswordDto input) {
        if (passwordEncoder.matches(input.getOld_password(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(input.getNew_password()));

            userRepository.save(user);

            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.ok("Password was wrong");
    }
}