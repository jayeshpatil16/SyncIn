package com.example.SyncIn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey()
    {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String id)
    {
        long now = System.currentTimeMillis();
        long expiry = now + expiration;

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractId(String token)
    {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public boolean isTokenValid(String token, Long id)
    {
        try {
            Long tokenId = extractId(token);
            return Objects.equals(tokenId, id);
        } catch (Exception e) {
            return false;
        }
    }
}
