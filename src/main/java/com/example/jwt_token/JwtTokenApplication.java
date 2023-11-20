package com.example.jwt_token;

import com.example.jwt_token.security.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication(scanBasePackages = "com.example.jwt_token")
@Import(JwtConfig.class)
@ComponentScan("com.example.jwt_token")
@EntityScan(basePackages = "com.example.jwt_token.entity")
public class JwtTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run (JwtTokenApplication.class, args);
    }

}
