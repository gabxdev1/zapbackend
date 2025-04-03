package br.com.gabxdev.model;

import br.com.gabxdev.model.pk.UserGroupId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRead {

    @EmbeddedId
    private UserGroupId id;

    private Instant readAt;
}
