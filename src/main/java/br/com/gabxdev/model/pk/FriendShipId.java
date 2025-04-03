package br.com.gabxdev.model.pk;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class FriendShipId {

    private Long userId1;

    private Long userId2;
}
