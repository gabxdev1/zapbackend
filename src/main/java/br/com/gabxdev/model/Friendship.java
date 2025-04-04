package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.pk.FriendshipId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@Table(name = "friendships")
public class Friendship extends Auditable {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne
    @MapsId("userId1")
    @JoinColumn(name = "user_id1", nullable = false, updatable = false)
    private User user1;

    @ManyToOne
    @MapsId("userId2")
    @JoinColumn(name = "user_id2", nullable = false, updatable = false)
    private User user2;
}
