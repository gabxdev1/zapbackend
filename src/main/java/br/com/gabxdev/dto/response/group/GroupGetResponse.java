package br.com.gabxdev.dto.response.group;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;

import java.util.List;

public record GroupGetResponse(

        Long id,

        String name,

        String description,

        List<GroupMemberPostResponse> members,

        AuditFullDetailsResponse audit
) {
}
