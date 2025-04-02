package br.com.gabxdev.model;

import br.com.gabxdev.model.pk.FriendShipId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "friendshipId")
@Builder
@Table(name = "friendships")
public class FriendShip { //implements audit

    @EmbeddedId
    private FriendShipId friendshipId;

    @ManyToOne
    @MapsId("userId1")
    private User user1;

    @ManyToOne
    @MapsId("userId2")
    private User user2;

    private boolean blocked;
}
