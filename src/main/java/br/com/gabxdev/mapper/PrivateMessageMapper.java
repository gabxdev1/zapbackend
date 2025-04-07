package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.response.privateMessage.PrivateMessageReadResponse;
import br.com.gabxdev.dto.response.privateMessage.PrivateMessageSendResponse;
import br.com.gabxdev.model.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuditMapper.class})
public interface PrivateMessageMapper {

    @Mappings({
            @Mapping(source = ".", target = "audit"),
            @Mapping(source = "privateMessage.message.content", target = "content"),
            @Mapping(source = "privateMessage.message.readAt", target = "readAt"),
            @Mapping(source = "privateMessage.message.receivedAt", target = "receivedAt"),
            @Mapping(source = "privateMessage.message.status", target = "status")
    })
    PrivateMessageSendResponse toPrivateMessageSendResponse(PrivateMessage privateMessage);

    @Mappings({
            @Mapping(source = "privateMessage.message.readAt", target = "readAt"),
            @Mapping(source = "privateMessage.message.status", target = "status")
    })
    PrivateMessageReadResponse toPrivateMessageReadResponse(PrivateMessage privateMessage);
}
