package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.mapper.GroupMessageMapper;
import br.com.gabxdev.model.GroupMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMessageStatusNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    private final GroupMessageMapper groupMessageMapper;

    public void notifyNewGroupStatus(GroupMessage groupMessage) {
        var response = groupMessageMapper.toGroupMessageResponse(groupMessage);

        messagingTemplate.convertAndSend(
                "/topic/group-" + groupMessage.getGroup().getId(),
                response
        );
    }
}
