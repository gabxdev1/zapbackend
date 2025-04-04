package br.com.gabxdev.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBlockId {

    @Column(name = "blocker_id", nullable = false)
    private Long blocker;

    @Column(name = "blocked_id", nullable = false)
    private Long blocked;
}
