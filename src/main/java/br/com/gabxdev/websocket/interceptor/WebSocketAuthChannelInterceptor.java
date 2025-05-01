package br.com.gabxdev.websocket.interceptor;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final GroupMembershipRules groupMembershipRules;
    private final AuthUtil authUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            var destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/topic/group")) {
                var auth = accessor.getUser();
                authUtil.setAuthenticationContext(auth);
                var currentUserId = authUtil.getCurrentUser().getId();

                var groupIdStr = destination.substring("/topic/group-".length());
                var groupId = Long.parseLong(groupIdStr);
                try {
                    groupMembershipRules.assertUserIsMemberOfGroupThrowForbidden(groupId, currentUserId);
                } catch (ForbiddenException e) {
                    return buildErrorMessage(e.getMessage(), accessor.getSessionId());
                }
            }
        }

        return message;
    }

    private Message<?> buildErrorMessage(String errorMessage, String sessionId) {
        var accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setSessionId(sessionId);
        accessor.setMessage(errorMessage);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

}
