package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(ignore = true, target = "user.id")
    void toEntity( User user,@MappingTarget UserEntity entity);

    UserEntity toEntity(RegisterReq req);

    UserEntity toDTO(LoginReq loginReq);

    UserEntity toDTO(RegisterReq req);

    @Mapping(source = "username", target = "email")
    User toDTO(UserEntity userEntity);

}
