package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class GroupMessage extends Auditable {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false, updatable = false)
    private Group group;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;
}
