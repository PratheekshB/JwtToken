package com.example.jwt_token.service;

import com.example.jwt_token.dto.UserRegistrationRequest;
import com.example.jwt_token.entity.User;
import com.example.jwt_token.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername()) ||
                userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new DuplicateRequestException ("Username or email already exists");
        }
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Collections.singleton("ROLE_USER"));
        userRepository.save (user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException ("User not found with username: " + username));
    }


    public String getRoleByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Assuming the roles are stored as a Set<String> in the User entity
        Set<String> roles = user.getRoles();

        // For simplicity, assuming a user has a single role
        if (!roles.isEmpty()) {
            return roles.iterator().next();
        } else {
            return "ROLE_USER"; // Default role if the user has no roles
        }
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user details from the database based on the provided username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // You may need to convert your User entity to a UserDetails implementation
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles ().toString ())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
