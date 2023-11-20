package com.example.jwt_token.service;

import com.example.jwt_token.dto.UserRegistrationRequest;
import com.example.jwt_token.entity.Role;
import com.example.jwt_token.entity.User;
import com.example.jwt_token.repository.RoleRepository;
import com.example.jwt_token.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername (registrationRequest.getUsername ( )) ||
                userRepository.existsByEmail (registrationRequest.getEmail ( ))) {
            throw new DuplicateRequestException ("Username or email already exists");
        }
        User user = new User ( );
        user.setUsername (registrationRequest.getUsername ( ));
        user.setPassword (passwordEncoder.encode (registrationRequest.getPassword ( )));

        List<String> roleNames = registrationRequest.getRoles ( );

        Set<Role> roles = roleNames.stream ( )
                .map (roleName -> {
                    try {
                        return roleRepository.findByName (roleName)
                                .orElseThrow (() -> new RoleNotFoundException ("Role not found: " + roleName));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException (e);
                    }
                })
                .collect (Collectors.toSet ( ));

        user.setRoles (roles);
        userRepository.save (user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername (username)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found with username: " + username));
    }


    public Set<Role> getRolesByUsername(String username) {
        User user = userRepository.findByUsername (username)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found with username: " + username));
        return user.getRoles ( );
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user details from the database based on the provided username
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
