package br.com.gabxdev.notification.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GroupNewMemberNotification(
        Long groupId,

        Set<Long> newMembersIds

) {
}
