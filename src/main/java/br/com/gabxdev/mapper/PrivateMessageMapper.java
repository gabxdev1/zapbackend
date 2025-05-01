package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.chat.MessageSummaryResponse;
import br.com.gabxdev.dto.response.private_message.PrivateMessageResponse;
import br.com.gabxdev.model.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface PrivateMessageMapper {

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.sender.id", target = "senderId"),
            @Mapping(source = "privateMessage.recipient", target = "recipient"),
            @Mapping(source = "privateMessage.message.content", target = "content"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.message.readAt", target = "readAt"),
            @Mapping(source = "privateMessage.message.receivedAt", target = "receivedAt"),
    })
    PrivateMessageResponse toPrivateMessageResponse(PrivateMessage privateMessage);

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.sender.id", target = "senderId"),
            @Mapping(source = "privateMessage.recipient", target = "recipient"),
            @Mapping(source = "privateMessage.message.content", target = "content"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.message.readAt", target = "readAt"),
            @Mapping(source = "privateMessage.message.receivedAt", target = "receivedAt"),
    })
    List<PrivateMessageResponse> toPrivateMessageResponse(List<PrivateMessage> privateMessage);

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.sender.id", target = "senderId"),
            @Mapping(source = "privateMessage.recipient.id", target = "recipientId"),
            @Mapping(source = "privateMessage.message.content", target = "content"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.message.readAt", target = "readAt"),
            @Mapping(source = "privateMessage.message.receivedAt", target = "receivedAt"),
    })
    MessageSummaryResponse toMessageSummaryResponse(PrivateMessage privateMessage);
}
