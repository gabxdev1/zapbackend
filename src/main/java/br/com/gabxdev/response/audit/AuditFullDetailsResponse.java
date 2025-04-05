package br.com.gabxdev.response.audit;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuditFullDetailsResponse(
        Instant createdAt,

        UserAuditDetailsResponse createdBy,

        Instant updatedAt,

        UserAuditDetailsResponse updatedBy
) {
}
