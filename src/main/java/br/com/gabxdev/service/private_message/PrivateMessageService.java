package br.com.gabxdev.service.private_message;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.dto.response.private_message.PrivateMessageResponse;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.PrivateMessageEmbeddable;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.repository.PrivateMessageRepository;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository repository;

    private final UserService userService;

    private final AuthUtil authUtil;

    private final PrivateMessageMapper privateMessageMapper;


    private PrivateMessage findByIdOrThrowNotFound(UUID messageId) {
        return repository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Private message %s not found".formatted(messageId)));
    }

    public PrivateMessage savePrivateMessage(PrivateMessageSendRequest request) {
        var senderId = authUtil.getCurrentUser().getId();
        var recipientId = request.recipientId();

        var sender = userService.findByIdOrThrowNotFound(senderId);

        var recipient = userService.findByIdOrThrowNotFound(recipientId);

        var message = PrivateMessageEmbeddable.builder()
                .content(request.content())
                .status(MessageStatus.SENT)
                .build();

        var privateMessage = PrivateMessage.builder()
                .id(request.messageId())
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .build();

        return repository.save(privateMessage);
    }

    @Transactional
    public Optional<PrivateMessage> updatePrivateMessageStatusSafely(UUID messageId, MessageStatus newStatus) {
        var message = findByIdOrThrowNotFound(messageId);

        if (!assertCurrentUserIsRecipient(message)) {
            return Optional.empty();
        }

        if (assertMessageNotRead(message)) {
            return Optional.empty();
        }

        var currentStatus = message.getMessage().getStatus();

        if (newStatus.status > currentStatus.status) {
            message.getMessage().setStatus(newStatus);

            var now = Instant.now();

            switch (newStatus) {
                case RECEIVED -> message.getMessage().setReceivedAt(now);
                case READ -> message.getMessage().setReadAt(now);
            }
        }

        return Optional.of(repository.save(message));
    }

    public List<PrivateMessageResponse> getAllPrivateMessages() {
        var currentUserId = authUtil.getCurrentUser().getId();

        var allPrivateMessage = repository.findAllPrivateMessage(currentUserId);

        return privateMessageMapper.toPrivateMessageResponse(allPrivateMessage);
    }

    public List<PrivateMessage> getAllOldMessages() {
        var currentUserId = authUtil.getCurrentUser().getId();

        return repository.findAllOldPrivateMessage(currentUserId);
    }

    private boolean assertCurrentUserIsRecipient(PrivateMessage message) {
        var currentUserId = authUtil.getCurrentUser().getId();

        return currentUserId.equals(message.getRecipient().getId());
    }

    public boolean assertMessageNotRead(PrivateMessage message) {
        return message.getMessage().getStatus().equals(MessageStatus.READ);
    }
}
