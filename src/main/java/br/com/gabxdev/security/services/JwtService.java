package br.com.gabxdev.security.services;

import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.net.URI;

import static br.com.gabxdev.commons.Constants.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository repository;

    public Authentication getAuthentication(Long userId, HttpServletRequest request) {
        var auth = createAuthentication(userId);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return auth;
    }

    public Authentication getAuthentication(Long userId) {
        return createAuthentication(userId);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Long userId) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(userId)));

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
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
