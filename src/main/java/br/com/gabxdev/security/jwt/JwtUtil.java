package br.com.gabxdev.security.jwt;

import br.com.gabxdev.model.enums.Role;
import br.com.gabxdev.properties.ZapBackendProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
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
                .subject(UUID.randomUUID().toString())
                .issuer("br.com.gabxdev")
                .claim("role", role)
                .claim("uid", userId)
                .audience().add("zapbackend-web-app")
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(expirationDate)
                .signWith(getSecretKey(), Jwts.SIG.HS384)
                .compact();
    }

    public Long extractUserIdAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .requireAudience("zapbackend-web-app")
                .requireIssuer("br.com.gabxdev")
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("uid", Long.class);
    }

    public SecretKey getSecretKey() {
        var secretKey = properties.getJwt().getSecretKey();
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
