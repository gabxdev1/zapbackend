package br.com.gabxdev.mapper;

import br.com.gabxdev.model.User;
import br.com.gabxdev.request.RegisterPostRequest;
import br.com.gabxdev.response.RegisterPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", expression = "java(br.com.gabxdev.model.enums.Role.ROLE_USER)")
    User toEntity(RegisterPostRequest request);

    RegisterPostResponse toRegisterPostResponse(User user);
}
