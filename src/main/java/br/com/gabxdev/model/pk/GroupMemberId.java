package br.com.gabxdev.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GroupMemberId {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_id", nullable = false)
    private Long groupId;
}
