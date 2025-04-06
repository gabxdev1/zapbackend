package br.com.gabxdev.service;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.MessageEmbeddable;
import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.repository.PrivateMessageRepository;
import br.com.gabxdev.request.privateMessage.PrivateMessageSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository repository;

    private final UserService userService;

    private final AuthUtil authUtil;

    private PrivateMessage findByIdOrThrowNotFound(Long messageId) {
        return repository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Private message %d not found".formatted(messageId)));
    }

    public PrivateMessage savePrivateMessage(PrivateMessageSendRequest request) {
        var senderId = authUtil.getCurrentUser().getId();
        var recipientId = request.recipientId();

        var sender = userService.findByIdOrThrowNotFound(senderId);

        var recipient = userService.findByIdOrThrowNotFound(recipientId);

        var message = MessageEmbeddable.builder()
                .content(request.content())
                .status(MessageStatus.SENT)
                .build();

        var privateMessage = PrivateMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .build();

        return repository.save(privateMessage);
    }

    public PrivateMessage updatePrivateMessageStatusAndReadAt(Long privateMessageId, MessageStatus status) {
        var message = findByIdOrThrowNotFound(privateMessageId);

        message.getMessage().setStatus(status);
        message.getMessage().setReadAt(Instant.now());

        return repository.save(message);
    }

    public PrivateMessage updatePrivateMessageStatusAndReceivedAt(Long privateMessageId, MessageStatus status) {
        var message = findByIdOrThrowNotFound(privateMessageId);

        message.getMessage().setStatus(status);
        message.getMessage().setReceivedAt(Instant.now());

        return repository.save(message);
    }
}
