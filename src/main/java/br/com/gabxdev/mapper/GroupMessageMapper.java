package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.group_message.GroupMessageResponse;
import br.com.gabxdev.dto.response.group_message.GroupMessageStatusResponse;
import br.com.gabxdev.model.GroupMessage;
import br.com.gabxdev.model.GroupMessageStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface GroupMessageMapper {

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "groupMessage.id", target = "messageId"),
            @Mapping(source = "groupMessage.group.id", target = "groupId"),
            @Mapping(source = "groupMessage.content", target = "content"),
            @Mapping(source = "groupMessage.status", target = "status"),
    })
    GroupMessageResponse toGroupMessageResponse(GroupMessage groupMessage);

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "groupMessage.id", target = "messageId"),
            @Mapping(source = "groupMessage.group.id", target = "groupId"),
            @Mapping(source = "groupMessage.content", target = "content"),
            @Mapping(source = "groupMessage.status", target = "status"),
    })
    List<GroupMessageResponse> toGroupMessageResponse(List<GroupMessage> groupMessage);


    @Mappings({
            @Mapping(source = "groupMessageStatus.user.id", target = "userId"),
            @Mapping(source = "groupMessageStatus.message.sender.id", target = "senderId"),
            @Mapping(source = "groupMessageStatus.user.firstName", target = "firstName"),
            @Mapping(source = "groupMessageStatus.user.lastName", target = "lastName"),
            @Mapping(source = "groupMessageStatus.message.id", target = "messageId"),
            @Mapping(source = "groupMessageStatus.message.group.id", target = "groupId"),
            @Mapping(source = "groupMessageStatus.status", target = "messageStatus"),
            @Mapping(source = "groupMessageStatus.receivedAt", target = "receivedAt"),
            @Mapping(source = "groupMessageStatus.readAt", target = "readAt"),
    })
    GroupMessageStatusResponse toGroupMessageStatusResponse(GroupMessageStatus groupMessageStatus);

    List<GroupMessageStatusResponse> toGroupMessageStatusResponse(List<GroupMessageStatus> groupMessageStatus);
}
