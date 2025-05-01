package br.com.gabxdev.mapper;

import br.com.gabxdev.model.UserPresence;
import br.com.gabxdev.notification.dto.UserPresenceStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPresenceMapper {

    @Mapping(target = "userId", source = "user.id")
    UserPresenceStatusResponse toUserPresenceStatusResponse(UserPresence userPresence);

    List<UserPresenceStatusResponse> toUserPresenceStatusResponseList(List<UserPresence> userPresence);
}
