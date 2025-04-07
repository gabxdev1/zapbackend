package br.com.gabxdev.dto.response.groupMember;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupMemberPostRequest(

        @NotNull
        @Min(1)
        Long groupId,

        @NotNull
        Set<Long> membersId
) {
}
