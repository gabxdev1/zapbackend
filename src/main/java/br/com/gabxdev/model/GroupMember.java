package br.com.gabxdev.model;


import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.pk.GroupMemberId;
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
    private GroupMemberId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false, updatable = false)
    private Group group;

    private boolean isAdmin;
}
