package com.example.jwt_token.presentation;

import com.example.jwt_token.dto.*;
import com.example.jwt_token.entity.User;
import com.example.jwt_token.repository.UserRepository;
import com.example.jwt_token.security.JwtTokenProvider;
import com.example.jwt_token.service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController() {
    }


    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername (username)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found with username: " + username));
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername ( );
        String password = loginRequest.getPassword ( );

        User user = loadUserByUsername (username);
        if (passwordEncoder.matches (password, user.getPassword ( ))) {
            String token = jwtTokenProvider.generateToken (username, Collections.emptySet ( ));
            return ResponseEntity.ok (new JwtAuthenticationResponse (token));
        } else {
            return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body ("Invalid username or password");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws SignatureException {
        String existingToken = refreshTokenRequest.getRefreshToken ( );
        if (jwtTokenProvider.validateToken (existingToken)) {
            String username = jwtTokenProvider.getUsernameFromToken (existingToken);
            UserDetails userDetails = userService.loadUserByUsername (username);

            Set<String> roleNames = userService.getRolesByUsername (username).stream ( )
                    .map (role -> role.getName ( ).toString ( ))
                    .collect (Collectors.toSet ( ));

            String newToken = jwtTokenProvider.generateToken (username, roleNames);
            return ResponseEntity.ok (new JwtAuthenticationResponse (newToken));
        } else {
            return ResponseEntity.status (401).body ("Invalid refresh token");
        }
    }


    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            userService.registerUser (registrationRequest);
            return ResponseEntity.ok ("User registered successfully");
        } catch (DuplicateRequestException e) {
            return ResponseEntity.badRequest ( ).body (e.getMessage ( ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        String tokenToInvalidate = logoutRequest.getAccessToken ( );
        if (jwtTokenProvider.isTokenBlacklisted (tokenToInvalidate)) {
            return ResponseEntity.badRequest ( ).body ("Token is already invalidated");
        } else {
            jwtTokenProvider.addToBlacklist (tokenToInvalidate);
            return ResponseEntity.ok ("Logout successful");
        }
    }
}
