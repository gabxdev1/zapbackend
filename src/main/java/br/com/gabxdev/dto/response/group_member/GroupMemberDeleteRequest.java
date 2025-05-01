package br.com.gabxdev.dto.response.group_member;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GroupMemberDeleteRequest(

        @NotNull
        @Min(1)
        Long groupId,

        @NotNull
        @Min(1)
        Long memberToRemoveId
) {
}
