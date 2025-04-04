package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.pk.UserBlockId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserBlock extends Auditable {

    @EmbeddedId
    private UserBlockId id;

    @ManyToOne
    @MapsId("blocker")
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne
    @MapsId("blocked")
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    @Column(nullable = false)
    private boolean isBlocked;
}
