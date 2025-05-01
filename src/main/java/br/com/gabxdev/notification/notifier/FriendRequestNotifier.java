package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.notification.dto.ReceivedPendingFriendRequestNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendRequestNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(ReceivedPendingFriendRequestNotifier newRequest, String email) {
        messagingTemplate.convertAndSendToUser(
                email,
                "/queue/new-friend-request",
                newRequest);
    }
}

