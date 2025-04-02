package br.com.gabxdev.security;

import br.com.gabxdev.service.CustomDetailsService;
import br.com.gabxdev.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static br.com.gabxdev.commons.Constants.WHITE_LIST;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomDetailsService customDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJwt = retrieveToken(request);
        var email = jwtUtil.extractEmail(tokenJwt);
        var user = customDetailsService.loadUserByUsername(email);


        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!(header != null && header.startsWith("Bearer "))) {
            throw new RuntimeException("Invalid token");
        }
        return header.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var requestURI = request.getRequestURI();

        return WHITE_LIST.contains(requestURI);
    }
}
