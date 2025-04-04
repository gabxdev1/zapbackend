package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.model.pk.GroupMemberId;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Group extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<GroupMember> members;

    public void addMember(User user, boolean isModerator) {
        var id = GroupMemberId.builder().userId(user.getId()).groupId(this.getId()).build();

        var groupMember = GroupMember.builder().id(id).user(user).group(this).isModerator(isModerator).build();

        members.add(groupMember);
    }
}
