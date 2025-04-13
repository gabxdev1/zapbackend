package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class GroupMessage extends Auditable {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false, updatable = false)
    private Group recipient;

    @Embedded
    private MessageEmbeddable message;
}
