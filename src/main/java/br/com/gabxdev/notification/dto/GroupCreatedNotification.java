package br.com.gabxdev.notification.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GroupCreatedNotification(
        Long groupId,

        Set<Long> membersId
) {
}
