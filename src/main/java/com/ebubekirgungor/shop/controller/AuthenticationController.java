package com.ebubekirgungor.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.LoginUserDto;
import com.ebubekirgungor.shop.model.User.RegisterUserDto;
import com.ebubekirgungor.shop.response.LoginResponse;
import com.ebubekirgungor.shop.service.AuthenticationService;
import com.ebubekirgungor.shop.service.JwtService;
import com.ebubekirgungor.shop.util.CookieUtils;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto,
            HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        CookieUtils.addCookie(response, "JWT_TOKEN", jwtToken, 24 * 60 * 60);
        return ResponseEntity.ok(loginResponse);
    }
}