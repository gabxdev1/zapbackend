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
public class UserGroupId {

    private Long userId;

    private Long groupId;
}
