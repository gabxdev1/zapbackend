package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.chat.MessageSummaryResponse;
import br.com.gabxdev.dto.response.privateMessage.PrivateMessageGetResponse;
import br.com.gabxdev.dto.response.privateMessage.PrivateMessageNotificationResponse;
import br.com.gabxdev.model.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface PrivateMessageMapper {

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
    PrivateMessageNotificationResponse toPrivateMessageSendResponse(PrivateMessage privateMessage);

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
    PrivateMessageGetResponse toPrivateMessageGetResponse(PrivateMessage privateMessage);

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
