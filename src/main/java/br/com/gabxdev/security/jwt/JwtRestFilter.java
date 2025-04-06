package br.com.gabxdev.security.jwt;

import br.com.gabxdev.exception.ApiError;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

import static br.com.gabxdev.commons.Constants.WHITE_LIST;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final JwtUtil jwtUtil;

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        log.debug("Received request to check if token is valid");

        try {
            var tokenFromRequest = jwtService.getTokenFromRequest(request);

            var userId = jwtUtil.extractUserIdAndValidate(tokenFromRequest);

            var auth = jwtService.getAuthentication(userId, request);

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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var requestURI = request.getRequestURI();

        return WHITE_LIST.contains(requestURI) || requestURI.startsWith("/zapbackend-ws");
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
