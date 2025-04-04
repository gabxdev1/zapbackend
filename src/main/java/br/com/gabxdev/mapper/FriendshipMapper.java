package br.com.gabxdev.mapper;

import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.request.FriendshipDeleteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendshipMapper {

    @Mapping(target = "userId1", expression = "java(getIdCurrentUser())")
    @Mapping(target = "userId2", source = "userId")
    FriendshipId toFriendshipId(FriendshipDeleteRequest request);

    default Long getIdCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}
