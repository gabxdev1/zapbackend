package br.com.gabxdev.dto.response.group;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;

public record GroupMemberPostResponse(

        UserPostResponse user,

        boolean moderator,

        AuditFullDetailsResponse audit
) {
}
