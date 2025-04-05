package br.com.gabxdev.response.groupMember;

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
