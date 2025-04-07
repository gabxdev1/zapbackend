package br.com.gabxdev.dto.response.groupMember;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GroupMemberDemotePostRequest(
        @NotNull
        @Min(1)
        Long groupId,

        @NotNull
        @Min(1)
        Long userToDemoteId
) {
}
