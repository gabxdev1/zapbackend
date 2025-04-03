package br.com.gabxdev.security.jwt;

import br.com.gabxdev.exception.ApiError;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

import static br.com.gabxdev.commons.Constants.WHITE_LIST;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        log.debug("Received request to check if token is valid");

        try {
            var tokenJwt = retrieveToken(request);
            var userId = jwtUtil.extractUserId(tokenJwt);
            var user = repository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User %d not found".formatted(userId)));

            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.debug("Successfully authenticated user");

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.debug("Error checking if token is valid '{}'", e.getMessage());

            throwError(HttpStatus.UNAUTHORIZED, "Invalid or expired token.", response, request);
        } catch (NotFoundException e) {
            log.debug("Error authenticating user '{}'", e.getMessage());

            throwError(HttpStatus.NOT_FOUND, e.getMessage(), response, request);
        } catch (ServletException e) {
            log.debug("Internal error occurred while checking if token is valid '{}'", e.getMessage());

            e.printStackTrace();

            throwError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), response, request);
        }
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!(header != null && header.startsWith("Bearer ") && !header.substring(7).isEmpty())) {
            throw new JwtException("Invalid token.");
        }
        return header.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var requestURI = request.getRequestURI();

        return WHITE_LIST.contains(requestURI);
    }

    private void throwError(HttpStatus status, String message, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        var error = ApiError.builder()
                .status(status.value())
                .path(request.getRequestURI())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now())
                .build();

        response.getWriter().write(mapper.writeValueAsString(error));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
