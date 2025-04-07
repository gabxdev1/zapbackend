package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.request.FriendRequestDeleteRequest;
import br.com.gabxdev.dto.request.FriendRequestPostRequest;
import br.com.gabxdev.dto.request.FriendRequestPutRequest;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.FriendRequestId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendRequestMapper {

    @Mapping(target = "senderId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestPostRequest request);

    @Mapping(target = "receiverId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestPutRequest request);

    @Mapping(target = "receiverId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestDeleteRequest request);

    default Long getIdCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}
