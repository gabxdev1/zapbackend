package br.com.gabxdev.mapper;

import br.com.gabxdev.dto.request.RegisterPostRequest;
import br.com.gabxdev.dto.request.UserPutRequest;
import br.com.gabxdev.dto.response.RegisterPostResponse;
import br.com.gabxdev.dto.response.audit.UserAuditDetailsResponse;
import br.com.gabxdev.dto.response.user.UserGetResponse;
import br.com.gabxdev.dto.response.user.UserPutResponse;
import br.com.gabxdev.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", expression = "java(br.com.gabxdev.model.enums.Role.ROLE_USER)")
    User toEntity(RegisterPostRequest request);

    @Mapping(target = "role", expression = "java(br.com.gabxdev.model.enums.Role.ROLE_USER)")
    @Mapping(target = "id", expression = "java(getIdCurrentUser())")
    User toEntity(UserPutRequest request);

    RegisterPostResponse toRegisterPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> user);

    UserPutResponse toUserPutResponse(User user);

    UserAuditDetailsResponse toUserAuditDetailsResponse(User user);

    default Long getIdCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}
