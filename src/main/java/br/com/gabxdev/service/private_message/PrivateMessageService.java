package br.com.gabxdev.service.private_message;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.dto.response.privateMessage.PrivateMessageGetResponse;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.model.MessageEmbeddable;
import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.repository.PrivateMessageRepository;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
                .id(request.messageId())
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .build();

        return repository.save(privateMessage);
    }

    @Transactional
    public PrivateMessage updatePrivateMessageStatusSafely(UUID messageId, MessageStatus newStatus) {
        var message = findByIdOrThrowNotFound(messageId);

        var currentStatus = message.getMessage().getStatus();

        if (newStatus.status > currentStatus.status) {
            message.getMessage().setStatus(newStatus);

            var now = Instant.now();

            switch (newStatus) {
                case RECEIVED -> message.getMessage().setReceivedAt(now);
                case READ -> message.getMessage().setReadAt(now);
            }
        }

        return repository.save(message);
    }

    public Slice<PrivateMessageGetResponse> getMessagesBetweenUsers(Long receivedId, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Long currentUserId = authUtil.getCurrentUser().getId();

        return repository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByCreatedAtDesc(
                        currentUserId, receivedId, receivedId, currentUserId, pageable
                )
                .map(privateMessageMapper::toPrivateMessageGetResponse);
    }
}
