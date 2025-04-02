package br.com.gabxdev.model;


import br.com.gabxdev.model.pk.UserGroupId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

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
}
