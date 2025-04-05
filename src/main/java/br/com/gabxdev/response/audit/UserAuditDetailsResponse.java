package br.com.gabxdev.response.audit;

public record UserAuditDetailsResponse(
        Long id,

        String firstName,

        String lastName
) {
}
