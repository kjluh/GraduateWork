package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserSecurity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Маппинг из ДТО User в сущность UserEntity
     * @param user из ДТО
     * @param entity в сущность
     */
    @Mapping(ignore = true, target = "entity.id")
    void toEntity( User user,@MappingTarget UserEntity entity);

    /**
     * Маппинг из ДТО RegisterReq в сущность UserEntity
     * @param req из RegisterReq
     * @return UserEntity
     */
    UserEntity toEntity(RegisterReq req);

    /**
     * Маппинг из ДТО LoginReq в сущность UserEntity
     * @param loginReq из LoginReq
     * @return UserEntity
     */
    UserEntity toDTO(LoginReq loginReq);

    /**
     * Маппинг из сущности UserEntity в ДТО User
     * @param userEntity из UserEntity
     * @return User
     */
    @Mapping(source = "username", target = "email")
    User toDTO(UserEntity userEntity);

    /**
     * Маппинг из сущности UserEntity в ДТО UserSecurity
     * @param userEntity из UserEntity
     * @return UserSecurity
     */
    @Mapping(source = "username", target = "login")
    UserSecurity toDTOSecurity(UserEntity userEntity);

}
