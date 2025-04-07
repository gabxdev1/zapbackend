package br.com.gabxdev.mapper;

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
            @Mapping(source = "privateMessage.message.content", target = "content"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
    })
    PrivateMessageNotificationResponse toPrivateMessageSendResponse(PrivateMessage privateMessage);
}
