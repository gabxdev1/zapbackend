package br.com.gabxdev.mapper;

import br.com.gabxdev.Audit.Auditable;
import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.dto.response.audit.UserAuditDetailsResponse;
import br.com.gabxdev.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AuditMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserMapper userMapper;

    public AuditFullDetailsResponse toAuditFullDetailsResponse(Auditable auditable) {
        if (auditable == null) return null;

        var audit = AuditFullDetailsResponse.builder();

        audit.createdAt(auditable.getCreatedAt());
        audit.updatedAt(auditable.getUpdatedAt());

        var createdBy = getUser(auditable.getCreatedBy());
        var updatedBy = getUser(auditable.getUpdatedBy());

        audit.createdBy(createdBy);
        audit.updatedBy(updatedBy);

        return audit.build();
    }

    private UserAuditDetailsResponse getUser(Long id) {
        if (id == null) return null;

        return userRepository.findById(id)
                .map(u -> userMapper.toUserAuditDetailsResponse(u))
                .orElse(null);
    }
}
