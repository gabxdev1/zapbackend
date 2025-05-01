package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.private_message.MessageStatusNotification;
import br.com.gabxdev.model.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mappings({
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.createdAt", target = "timestamp"),
    })
    MessageStatusNotification toMessageStatusNotificationAck(PrivateMessage privateMessage);

    @Mappings({
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.message.readAt", target = "timestamp")
    })
    MessageStatusNotification toMessageStatusNotificationRead(PrivateMessage privateMessage);

    @Mappings({
            @Mapping(source = "privateMessage.id", target = "messageId"),
            @Mapping(source = "privateMessage.message.status", target = "status"),
            @Mapping(source = "privateMessage.message.receivedAt", target = "timestamp")
    })
    MessageStatusNotification toMessageStatusNotificationReceived(PrivateMessage privateMessage);
}
