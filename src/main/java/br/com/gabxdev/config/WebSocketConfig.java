package br.com.gabxdev.config;

import br.com.gabxdev.properties.ZapBackendProperties;
import br.com.gabxdev.websocket.interceptor.JwtHandshakeInterceptor;
import br.com.gabxdev.websocket.interceptor.WebSocketAuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    private final ZapBackendProperties properties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(properties.getStompBrokerRelay().getRelayHost())
                .setRelayPort(properties.getStompBrokerRelay().getRelayPort())
                .setClientLogin(properties.getStompBrokerRelay().getClientLogin())
                .setClientPasscode(properties.getStompBrokerRelay().getClientPasscode())
                .setSystemLogin(properties.getStompBrokerRelay().getSystemLogin())
                .setSystemPasscode(properties.getStompBrokerRelay().getSystemPasscode());

        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/zapbackend-ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthChannelInterceptor);
    }
}
