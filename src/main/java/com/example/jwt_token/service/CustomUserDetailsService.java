package com.example.jwt_token.service;

import com.example.jwt_token.entity.User;
import com.example.jwt_token.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername (username)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found with username: " + username));

        // You may need to convert your User entity to a UserDetails implementation
        return org.springframework.security.core.userdetails.User
                .withUsername (user.getUsername ( ))
                .password (user.getPassword ( ))
                .authorities (user.getRoles ( ).toString ( ))
                .accountExpired (false)
                .accountLocked (false)
                .credentialsExpired (false)
                .disabled (false)
                .build ( );
    }
}