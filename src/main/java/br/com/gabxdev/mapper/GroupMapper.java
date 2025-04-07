package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.group.GroupGetResponse;
import br.com.gabxdev.dto.response.group.GroupMemberPostResponse;
import br.com.gabxdev.dto.response.group.GroupPostResponse;
import br.com.gabxdev.dto.response.group.GroupPutResponse;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.model.GroupMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface GroupMapper {

    @Mapping(source = ".", target = "audit")
    GroupPostResponse toGroupPostResponse(Group group);

    @Mapping(source = ".", target = "audit")
    GroupMemberPostResponse toGroupPostResponse(GroupMember member);

    @Mapping(source = ".", target = "audit")
    GroupGetResponse toGroupGetResponse(Group group);

    GroupPutResponse toGroupPutResponse(Group group);
}
