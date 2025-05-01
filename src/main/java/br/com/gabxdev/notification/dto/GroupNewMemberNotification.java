package br.com.gabxdev.notification.dto;

import lombok.Builder;

@Builder
public record GroupNewMemberNotification(
        Long groupId
) {
}
