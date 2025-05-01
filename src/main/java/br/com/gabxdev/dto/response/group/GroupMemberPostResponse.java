package br.com.gabxdev.dto.response.group;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.dto.response.user.UserGetResponse;

public record GroupMemberPostResponse(

        UserGetResponse user,

        boolean moderator,

        AuditFullDetailsResponse audit
) {
}
