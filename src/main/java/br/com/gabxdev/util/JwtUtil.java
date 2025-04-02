package br.com.gabxdev.util;

import br.com.gabxdev.enums.Role;
import br.com.gabxdev.properties.ZapBackendProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ZapBackendProperties properties;
    private final static SecretKey SECRET_KEY = Jwts.SIG.HS384.key().build();

    public String generateToken(Long userId, Role role) {
        var now = Instant.now();
        var expirationSeconds = properties.getJwt().getExpirationSeconds();
        var expirationDate = Date.from(now.plusSeconds(expirationSeconds));


        return Jwts.builder()
                .signWith(SECRET_KEY, Jwts.SIG.HS384)
                .subject(UUID.randomUUID().toString())
                .issuer("br.com.gabxdev")
                .claim("role", role)
                .claim("uid", userId)
                .audience().add("zapbackend-web-app")
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(expirationDate)
                .compact();
    }

    public Long extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .requireAudience("zapbackend-web-app")
                .requireIssuer("br.com.gabxdev")
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("uid", Long.class);
    }
}
