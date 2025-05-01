package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.request.friend_request.FriendRequestDeleteRequest;
import br.com.gabxdev.dto.request.friend_request.FriendRequestPostRequest;
import br.com.gabxdev.dto.request.friend_request.FriendRequestPutRequest;
import br.com.gabxdev.model.FriendRequest;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.FriendRequestId;
import br.com.gabxdev.notification.dto.ReceivedPendingFriendRequestNotifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendRequestMapper {

    @Mapping(target = "senderId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestPostRequest request);

    @Mapping(target = "receiverId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestPutRequest request);

    @Mapping(target = "receiverId", expression = "java(getIdCurrentUser())")
    FriendRequestId toFriendRequestId(FriendRequestDeleteRequest request);

    @Mappings({
            @Mapping(target = "status", source = "friendRequest.status"),
            @Mapping(target = "sender.id", source = "friendRequest.sender.id"),
            @Mapping(target = "sender.firstName", source = "friendRequest.sender.firstName"),
            @Mapping(target = "sender.lastName", source = "friendRequest.sender.lastName"),
            @Mapping(target = "sender.nickname", source = "friendRequest.sender.nickname"),
            @Mapping(target = "createdAt", source = "friendRequest.createdAt"),
    })
    ReceivedPendingFriendRequestNotifier toReceivedPendingFriendRequestNotifier(FriendRequest friendRequest);

    @Mappings({
            @Mapping(target = "status", source = "friendRequest.status"),
            @Mapping(target = "sender.id", source = "friendRequest.sender.id"),
            @Mapping(target = "sender.firstName", source = "friendRequest.sender.firstName"),
            @Mapping(target = "sender.lastName", source = "friendRequest.sender.lastName"),
            @Mapping(target = "sender.nickname", source = "friendRequest.sender.nickname"),
            @Mapping(target = "createdAt", source = "friendRequest.createdAt"),
    })
    List<ReceivedPendingFriendRequestNotifier> toReceivedPendingFriendRequestNotifier(List<FriendRequest> friendRequest);

    default Long getIdCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}
