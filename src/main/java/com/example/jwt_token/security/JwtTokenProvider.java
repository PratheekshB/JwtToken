package com.example.jwt_token.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtTokenProvider {

    private static String secret;
    private static Long expiration;

    public JwtTokenProvider(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

//    public static String generateToken(String username, String role) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("role", role)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .build ()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) throws SignatureException {
        Jwts.parser().setSigningKey(secret).build ().parseClaimsJws(token);
        return true;
    }

    private Set<String> blacklistedTokens = new HashSet<> ();


    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }


}
