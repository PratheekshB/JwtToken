package com.example.jwt_token.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public JwtTokenProvider(String jwtSecret, int jwtExpirationInMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.blacklistedTokens = blacklistedTokens;
    }

    public String generateToken(String username, Set<String> roles) {
        Date now = new Date ( );
        Date expiryDate = new Date (now.getTime ( ) + jwtExpirationInMs);

        return Jwts.builder ( )
                .setSubject (username)
                .claim ("roles", roles)
                .setIssuedAt (now)
                .setExpiration (expiryDate)
                .signWith (SignatureAlgorithm.HS512, jwtSecret)
                .compact ( );
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser ( )
                .setSigningKey (jwtSecret)
                .build ( )
                .parseClaimsJws (token)
                .getBody ( );

        return claims.getSubject ( );
    }

    public boolean validateToken(String token) throws SignatureException {
        Jwts.parser ( ).setSigningKey (jwtSecret).build ( ).parseClaimsJws (token);
        return true;
    }

    private Set<String> blacklistedTokens = new HashSet<> ( );


    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains (token);
    }

    public void addToBlacklist(String token) {
        blacklistedTokens.add (token);
    }


}
