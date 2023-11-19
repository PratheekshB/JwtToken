package com.example.jwt_token.security;
import com.example.jwt_token.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests ( )
                .requestMatchers ("/api/user/**").permitAll ( )
                .requestMatchers ("/private/**").authenticated ( )
                .and ()
                .formLogin ( )
                .loginPage ("/login")
                .defaultSuccessUrl ("/dashboard")
                .permitAll ( )
                .and ( )
                .logout ( )
                .logoutUrl ("/logout")
                .logoutSuccessUrl ("/login")
                .permitAll ( )
                .and ( )
                .csrf ( ).disable ( );

        return http.build ();
    }
}
