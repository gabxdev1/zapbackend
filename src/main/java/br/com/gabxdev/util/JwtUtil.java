package br.com.gabxdev.util;

import br.com.gabxdev.properties.ZapBackendProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ZapBackendProperties properties;
    private final static SecretKey SECRET_KEY = Jwts.SIG.HS384.key().build();

    public String generateToken(String email) {
        var now = Instant.now();
        var expirationSeconds = properties.getJwt().getExpirationSeconds();
        var expirationDate = Date.from(now.plusSeconds(expirationSeconds));

        return Jwts.builder()
                .issuer("br.com.gabxdev")
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(expirationDate)
                .signWith(SECRET_KEY, Jwts.SIG.HS384)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
