package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.pk.GroupMessageReadId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRead extends Auditable {

    @EmbeddedId
    private GroupMessageReadId id;

    @ManyToOne
    @MapsId("groupMessageId")
    @JoinColumn(name = "group_message_id", nullable = false, updatable = false)
    private GroupMessage message;

    @ManyToOne
    @MapsId(value = "userId")
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
