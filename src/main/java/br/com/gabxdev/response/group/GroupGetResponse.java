package br.com.gabxdev.response.group;

import br.com.gabxdev.response.audit.AuditFullDetailsResponse;

import java.util.List;

public record GroupGetResponse(

        Long id,

        String name,

        String description,

        List<GroupMemberPostResponse> members,

        AuditFullDetailsResponse audit
) {
}
