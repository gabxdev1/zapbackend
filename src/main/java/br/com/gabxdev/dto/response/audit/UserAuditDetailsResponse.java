package br.com.gabxdev.dto.response.audit;

public record UserAuditDetailsResponse(
        Long id,

        String firstName,

        String lastName
) {
}
