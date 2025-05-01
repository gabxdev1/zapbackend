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
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface GroupMapper {

    @Mapping(source = ".", target = "audit")
    GroupPostResponse toGroupPostResponse(Group group);

    @Mapping(source = ".", target = "audit")
    GroupMemberPostResponse toGroupPostResponse(GroupMember member);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "members", source = "members"),
            @Mapping(target = "audit", source = ".")
    })
    GroupGetResponse toGroupGetResponse(Group group);

    List<GroupGetResponse> toGroupGetResponse(List<Group> groups);

    GroupPutResponse toGroupPutResponse(Group group);
}
