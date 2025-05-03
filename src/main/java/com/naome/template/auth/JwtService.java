package com.naome.template.auth;

import com.naome.template.auth.exceptions.InvalidJwtException;
import com.naome.template.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {
    private final JwtConfig config;

    Jwt generateAccessToken(User user){
        return generateToken(user, config.getAccessTokenExpiration());
    }

    Jwt generateRefreshToken(User user){
        return generateToken(user, config.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration){
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("phoneNumber", user.getPhoneNumber())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        return new Jwt(claims, config.getSecretKey());
    }

    Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, config.getSecretKey());
        } catch (ExpiredJwtException ex) {
            log.debug("Token expired: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Token expired");
        } catch (SignatureException ex) {
            log.debug("Invalid token signature: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Invalid token signature");
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Invalid token: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Invalid token");
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
