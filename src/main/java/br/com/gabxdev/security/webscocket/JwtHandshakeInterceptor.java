package br.com.gabxdev.security.webscocket;

import br.com.gabxdev.security.jwt.JwtUtil;
import br.com.gabxdev.security.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        try {
            var tokenFromRequest = jwtService.getTokenFromRequest(request.getURI());

            var userId = jwtUtil.extractUserIdAndValidate(tokenFromRequest);

            var auth = jwtService.getAuthentication(userId);

            attributes.put("auth", auth);

            SecurityContextHolder.getContext().setAuthentication(auth);

            log.debug("WebSocket handshake authenticated successfully for userId {}", userId);

            return true;
        } catch (RuntimeException e) {
            log.warn("Failed to authenticate WebSocket handshake: {}", e.getMessage());

            SecurityContextHolder.clearContext();

            response.setStatusCode(HttpStatus.FORBIDDEN);

            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
