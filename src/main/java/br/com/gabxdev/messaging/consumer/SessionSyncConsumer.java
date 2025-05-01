package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.messaging.wrapper.TriggerWrapper;
import br.com.gabxdev.notification.notifier.SyncSessionNotifier;
import br.com.gabxdev.service.private_message.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.SESSION_SYNC;

@RequiredArgsConstructor
@Component
public class SessionSyncConsumer {

    private final AuthUtil authUtil;

    private final SyncSessionNotifier syncSessionNotifier;

    private final PrivateMessageService service;

    @RabbitListener(queues = SESSION_SYNC)
    public void processOldMessage(TriggerWrapper triggerWrapper) {
        authUtil.createAuthenticationAndSetAuthenticationContext(
                triggerWrapper.senderId(),
                triggerWrapper.senderEmail(),
                triggerWrapper.roles());

        var messages = service.getAllOldMessages();

        syncSessionNotifier.notifyUser(messages, triggerWrapper.senderEmail());
    }
}
