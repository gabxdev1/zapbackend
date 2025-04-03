package br.com.gabxdev.mapper;

import br.com.gabxdev.model.pk.FriendRequestId;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.request.FriendRequestDeleteRequest;
import br.com.gabxdev.request.FriendRequestPostRequest;
import br.com.gabxdev.request.FriendRequestPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendRequestMapper {

    FriendRequestId toFriendRequestId(FriendRequestPostRequest request);

    FriendRequestId toFriendRequestId(FriendRequestPutRequest request);

    FriendRequestId toFriendRequestId(FriendRequestDeleteRequest request);
}
