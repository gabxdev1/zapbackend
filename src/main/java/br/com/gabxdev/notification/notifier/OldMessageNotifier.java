package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.model.PrivateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OldMessageNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    private final PrivateMessageMapper privateMessageMapper;

    public void notifyUser(List<PrivateMessage> messages, String email) {
        privateMessageMapper
                .toPrivateMessageGetResponse(messages)
                .forEach(privateMessage ->
                        messagingTemplate.convertAndSendToUser(
                                email,
                                "/queue/messages",
                                privateMessage
                        ));
    }
}
