package br.com.gabxdev.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class FriendshipId {

    @Column(name = "user1_id", nullable = false)
    private Long userId1;

    @Column(name = "user2_Id", nullable = false)
    private Long userId2;
}
