package br.com.gabxdev.service;

import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.MessageEmbeddable;
import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.repository.PrivateMessageRepository;
import br.com.gabxdev.repository.UserRepository;
import br.com.gabxdev.request.message.PrivateMessageSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository repository;

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    public void sendPrivateMessage(PrivateMessageSendRequest request, Principal principal) {

        var sender = userRepository.findByEmailIgnoreCase(principal.getName())
                .orElseThrow(() -> new NotFoundException("Sender %s not found".formatted(principal.getName())));

        var recipient = userRepository.findById(request.recipientId())
                .orElseThrow(() -> new NotFoundException("Recipient %d not found".formatted(request.recipientId())));

        var message = MessageEmbeddable.builder()
                .content(request.content())
                .status(MessageStatus.SENT)
                .build();

        var privateMessage = PrivateMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .build();

        var messageSaved = repository.save(privateMessage);

        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(),
                "/queue/messages",
                messageSaved
        );
    }
}
