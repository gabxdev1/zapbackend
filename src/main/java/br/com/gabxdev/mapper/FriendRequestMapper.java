package br.com.gabxdev.mapper;

import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.FriendRequestId;
import br.com.gabxdev.request.FriendRequestDeleteRequest;
import br.com.gabxdev.request.FriendRequestPostRequest;
import br.com.gabxdev.request.FriendRequestPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendRequestMapper {

    @Mapping(target = "senderId", expression = "java(getAuthenticatedUserId())")
    FriendRequestId toFriendRequestId(FriendRequestPostRequest request);

    @Mapping(target = "receiverId", expression = "java(getAuthenticatedUserId())")
    FriendRequestId toFriendRequestId(FriendRequestPutRequest request);

    @Mapping(target = "receiverId", expression = "java(getAuthenticatedUserId())")
    FriendRequestId toFriendRequestId(FriendRequestDeleteRequest request);

    default Long getAuthenticatedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}
