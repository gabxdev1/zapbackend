package br.com.gabxdev.dto.response.audit;

import br.com.gabxdev.dto.response.user.UserGetResponse;
import lombok.Builder;

import java.time.Instant;

@Builder
public record AuditFullDetailsResponse(
        Instant createdAt,

        UserGetResponse createdBy,

        Instant updatedAt,

        UserGetResponse updatedBy
) {
}
