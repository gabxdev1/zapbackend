package br.com.gabxdev.model;

import br.com.gabxdev.model.enums.MessageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageEmbeddable {

    @Column(nullable = false, length = 2000)
    private String content;

    private Instant readAt;

    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

}
