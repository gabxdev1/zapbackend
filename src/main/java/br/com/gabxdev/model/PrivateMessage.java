package br.com.gabxdev.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false, updatable = false)
    private User recipient;

    @Embedded
    private MessageEmbeddable message;
}
