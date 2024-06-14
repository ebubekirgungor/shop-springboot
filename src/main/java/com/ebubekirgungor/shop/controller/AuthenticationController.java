package com.ebubekirgungor.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.LoginUserDto;
import com.ebubekirgungor.shop.model.User.RegisterUserDto;
import com.ebubekirgungor.shop.model.User.UpdatePasswordDto;
import com.ebubekirgungor.shop.service.AuthenticationService;
import com.ebubekirgungor.shop.service.JwtService;
import com.ebubekirgungor.shop.util.CookieUtils;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "${origin-url}", allowCredentials = "true")
@RequestMapping("${api-base}/auth")
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
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody LoginUserDto loginUserDto,
            HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        CookieUtils.addCookie(response, "jwt", jwtToken,
                loginUserDto.getRemember_me() ? 30 * 24 * 60 * 60 : 60 * 60);

        Map<String, String> loginResponse = new HashMap<>();
        loginResponse.put("status", "ok");
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, "jwt");

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/update_password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authenticationService.updatePassword((User) authentication.getPrincipal(), updatePasswordDto);
    }
}