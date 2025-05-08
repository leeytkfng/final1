package com.example.demo.userservice.component.impl;

import com.example.demo.userservice.component.TokenProvider;
import com.example.demo.userservice.entity.UserEntity;
import com.example.demo.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component("accessTokenProvider")
public class AccessTokenProviderImpl implements TokenProvider {
    private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long validityInMillis = 15 * 60 * 1000; // 15분
    private final UserRepository userRepository;

    public AccessTokenProviderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String createToken(String email) {
        // 액세스 토큰은 이메일과 함께 클레임에 역할(roles) 정보를 포함
        Claims claims = Jwts.claims().setSubject(email);
        userRepository.findByEmail(email).ifPresent(user -> {
            if(user.isAdmin()) claims.put("admin", true);
            claims.put("userid", user.getId());
        });
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public long getValidityInMillis() {
        return validityInMillis;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret) // 비밀키를 바이트 배열로 전달
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
