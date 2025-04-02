package br.com.gabxdev.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private User recipientUser;

    @ManyToOne
    @JoinColumn(name = "recipient_group_id")
    private Group recipientGroup;

    @Column(nullable = false, length = 999)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant sentAt;

    @PrePersist
    private void validateMessage() {
        if ((recipientUser == null && recipientGroup == null) ||
            (recipientUser != null && recipientGroup != null)) {
            throw new IllegalArgumentException("A message must have a recipient (user or group, but not both).");
        }
    }
}
