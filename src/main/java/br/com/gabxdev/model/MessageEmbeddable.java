package br.com.gabxdev.model;

import br.com.gabxdev.model.enums.MessageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Embeddable
public class MessageEmbeddable {

    @Column(nullable = false, length = 999)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant sentAt;

    private Instant readAt;

    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

}
