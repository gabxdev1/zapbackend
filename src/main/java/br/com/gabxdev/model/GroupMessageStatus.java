package br.com.gabxdev.model;

import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.model.pk.GroupMessageStatusId;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageStatus {

    @EmbeddedId
    private GroupMessageStatusId id;

    @ManyToOne
    @MapsId("groupMessageId")
    @JoinColumn(name = "group_message_id", nullable = false, updatable = false)
    private GroupMessage message;

    @ManyToOne
    @MapsId(value = "userId")
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    @Column(nullable = false, updatable = false)
    private Instant receivedAt;

    @Column(updatable = false)
    private Instant readAt;
}
