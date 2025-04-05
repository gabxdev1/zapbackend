package br.com.gabxdev.response.groupMember;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupMemberPostRequest(

        @NotNull
        Long groupId,

        @NotNull
        Set<Long> membersId
) {
}
