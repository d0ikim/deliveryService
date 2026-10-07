package com.sparta.delivery.global.security;

import com.sparta.delivery.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") // application.yml의 jwt.secret값을 필드에 주입
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @PostConstruct  // 스프링이 이 빈을 다 만든 직후 딱 "한번만" 실행되는 메서드
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)                  // 아이디(subject)
                .claim("role", role.name())   // 역할(role claim)
                .issuedAt(now)
                .expiration(expiryDate)             // 만료시간
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {   // 토큰 검증 -> 그 안의 claim들(username, role, 만료시간 등)을 꺼내는 메서드
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}