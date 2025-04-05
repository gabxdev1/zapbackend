package br.com.gabxdev.response.group;

import br.com.gabxdev.response.audit.AuditFullDetailsResponse;

public record GroupMemberPostResponse(

        UserPostResponse user,

        boolean moderator,

        AuditFullDetailsResponse audit
) {
}
