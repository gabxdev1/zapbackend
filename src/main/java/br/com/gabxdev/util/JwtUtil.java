package br.com.gabxdev.util;

import br.com.gabxdev.properties.ZapBackendProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretKeyBuilder;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ZapBackendProperties properties;

    public String generateToken(String email){
        var now = Instant.now();
        var expirationSeconds = properties.getJwt().getExpirationSeconds();
        var expirationDate = Date.from(now.plusSeconds(expirationSeconds));

        var secretKey = genSecretKey(properties.getJwt().getSecretKey());

        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        var secretKey = genSecretKey(properties.getJwt().getSecretKey());

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        //UnsupportedJwtException
        //JwtException
        //ExpiredJwtException
    }


    private SecretKey genSecretKey(String secretKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
