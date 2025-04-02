package br.com.gabxdev.security;

import br.com.gabxdev.exception.ApiError;
import br.com.gabxdev.repository.UserRepository;
import br.com.gabxdev.service.CustomDetailsService;
import br.com.gabxdev.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;

import static br.com.gabxdev.commons.Constants.WHITE_LIST;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            var tokenJwt = retrieveToken(request);
            var userId = jwtUtil.extractUserId(tokenJwt);
            var user = repository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            var error = ApiError.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .path(request.getRequestURI())
                    .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .message("Invalid or expired token.")
                    .timestamp(OffsetDateTime.now())
                    .build();

            response.getWriter().write(mapper.writeValueAsString(error));
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!(header != null && header.startsWith("Bearer ") && !header.substring(7).isEmpty())) {
            throw new RuntimeException("Invalid token.");
        }
        return header.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var requestURI = request.getRequestURI();

        return WHITE_LIST.contains(requestURI);
    }
}
