package br.com.gabxdev.model;


import br.com.gabxdev.model.pk.UserGroupId;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "group_members")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroupMember {

    @EmbeddedId
    private UserGroupId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("groupId")
    private Group group;

    private Instant joinedAt;

    private boolean isAdmin;
}
