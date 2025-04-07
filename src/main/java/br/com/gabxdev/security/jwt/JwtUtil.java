package br.com.gabxdev.security.jwt;

import br.com.gabxdev.model.enums.Role;
import br.com.gabxdev.properties.ZapBackendProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static br.com.gabxdev.commons.Constants.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ZapBackendProperties properties;

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

    public String getTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            throw new JwtException("Invalid JWT token");
        }

        return getTokenFromHeaderAuthorization(authHeader);
    }

    public String getTokenFromRequest(URI uri) {

        String query = uri.getQuery();

        if (query != null && query.startsWith("token=")) {
            return query.substring("token=".length());
        } else {
            throw new JwtException("Missing JWT token in query parameter");
        }
    }

    private String getTokenFromHeaderAuthorization(String header) {
        if (!(header.startsWith(BEARER_PREFIX) && !header.substring(7).isEmpty())) {
            throw new JwtException("Invalid token.");
        }

        return header.substring(7);
    }
}
