package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.notification.dto.UserPresenceStatusEvent;
import br.com.gabxdev.notification.notifier.UserPresenceNotifier;
import br.com.gabxdev.service.user.UserPresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.USER_PRESENCE_CHANGE;

@RequiredArgsConstructor
@Component
public class UserPresenceChangeConsumer {

    private final UserPresenceNotifier userPresenceNotifier;

    private final UserPresenceService userPresenceService;

    @RabbitListener(queues = USER_PRESENCE_CHANGE)
    public void consumePresenceChange(MessageWrapper<UserPresenceStatusEvent> messageWrapper) {
        var event = messageWrapper.request();

        var userChangeStatusId = messageWrapper.senderId();
        var useLastSeenAt = event.lastSeenAt();
        var userStatus = event.status();

        if (userStatus.equals(UserStatus.ONLINE)) {
            userPresenceService.markOnline(userChangeStatusId);
        }

        if (userStatus.equals(UserStatus.OFFLINE)) {
            userPresenceService.markOffline(userChangeStatusId, useLastSeenAt);
        }

        userPresenceNotifier.notifyUserPresenceChange(userChangeStatusId,
                userStatus,
                useLastSeenAt);
    }
}
