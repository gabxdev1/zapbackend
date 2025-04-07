package br.com.gabxdev.model;

import br.com.gabxdev.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "user")
public class UserPresence {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Instant lastSeenAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
