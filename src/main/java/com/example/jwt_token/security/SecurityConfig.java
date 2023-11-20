package com.example.jwt_token.security;

import com.example.jwt_token.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtConfig jwtConfig;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService (customUserDetailsService).passwordEncoder (passwordEncoder ( ));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ( );
    }

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Configuration
    public static class ApiSecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;
        private final CustomUserDetailsService customUserDetailsService;

        public ApiSecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
            this.jwtTokenProvider = jwtTokenProvider;
            this.customUserDetailsService = customUserDetailsService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf ( ).disable ( )
                    .authorizeHttpRequests ( )
                    .requestMatchers ("/api/user/login").permitAll ( )
                    .requestMatchers ("/api/user/register").permitAll ( )
                    .requestMatchers ("/api/user/refresh").permitAll ( )
                    .requestMatchers ("/api/public/**").permitAll ( )
                    .requestMatchers (HttpMethod.OPTIONS, "/**").permitAll ( )
                    .anyRequest ( ).authenticated ( )
                    .and ( )
                    .formLogin ( ).permitAll ( );
            return http.build ( );
        }
    }
}
