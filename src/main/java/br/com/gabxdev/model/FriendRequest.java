package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.enums.RequestStatus;
import br.com.gabxdev.model.pk.FriendRequestId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest extends Auditable {

    @EmbeddedId
    private FriendRequestId id;

    @ManyToOne
    @MapsId("senderId")
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne
    @MapsId("receiverId")
    @JoinColumn(name = "receiver_id", nullable = false, updatable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
