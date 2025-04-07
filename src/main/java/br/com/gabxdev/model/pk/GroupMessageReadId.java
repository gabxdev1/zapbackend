package br.com.gabxdev.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GroupMessageReadId {

    @Column(name = "group_message_id", nullable = false)
    private UUID groupMessageId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
