package br.com.gabxdev.model;


import br.com.gabxdev.Audit.Auditable;
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
@EqualsAndHashCode(of = "id", callSuper = false)
public class GroupMember extends Auditable {

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
