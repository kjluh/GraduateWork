package ru.skypro.homework.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.AvatarUserEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repositories.AvatarUserEntityRepository;
import ru.skypro.homework.repositories.UserEntityRepository;

import java.io.*;

/**
 * Сервис для работы с пользователями
 */
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    private final AvatarUserEntityRepository avatarUserEntityRepository;

    public UserService(UserEntityRepository userEntityRepository, AvatarUserEntityRepository avatarUserEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.avatarUserEntityRepository = avatarUserEntityRepository;
    }

    /**
     * Обновление пароля
     * @param newPass принимает сущность нового пароля
     * @param userName принимает логин пользователя
     * @return возвращает статус по смене 200 - ок, 401 ошибка
     */
    @Transactional(readOnly = true)
    public boolean updatePassword(NewPassword newPass, String userName) {
        UserEntity userEntity = getUserEntity(userName);
        if (userEntity.getPassword().equals(newPass.getCurrentPassword())) {
            userEntity.setPassword(newPass.getNewPassword());
            userEntityRepository.save(userEntity);
            return true;
        }
        return false;
    }

    /**
     * Возвращает сущность пользователя для отображения на странице
     * @param userName принимает логин
     * @return принимает логин пользователя
     */
    @Transactional(readOnly = true)
    public User getUser(String userName) {
        UserEntity userEntity = getUserEntity(userName);
        User thisUser = UserMapper.INSTANCE.toDTO(userEntity);
        if (null==userEntity.getAvatarUserEntity()){
            return thisUser;
        }
        thisUser.setImage("/users/avatar/" + userEntity.getAvatarUserEntity().getId() + "/db");

        return thisUser;
    }

    /**
     *
     * @param user принимает сущность пользователя с изменениями для обновления
     * @param userName принимает логин пользователя
     * @return возвращает обновленного пользователя
     */
    @Transactional
    public User updateUser(User user, String userName) {
        UserEntity userEntity = getUserEntity(userName);
        UserMapper.INSTANCE.toEntity(user, userEntity);
        userEntityRepository.save(userEntity);
        return UserMapper.INSTANCE.toDTO(userEntity);
    }

    /**
     *
     * @param image Мультипад файл картинка новой аватарки
     * @param userName принимает логин пользователя
     * @throws IOException
     */
    @Transactional // необходимо писать если вызываем сущность из бд с картинкой в параметрах с анат @lob
    public void updateAvatar(MultipartFile image, String userName) throws IOException {
        UserEntity userEntity = getUserEntity(userName);

        AvatarUserEntity avatar = avatarUserEntityRepository.findById(
                (userEntity.getId())).orElse(new AvatarUserEntity());
        avatar.setUserEntity(userEntity);
        avatar.setMediaType(image.getContentType());
        avatar.setData(image.getBytes());
        avatarUserEntityRepository.save(avatar);
        userEntity.setAvatarUserEntity(avatar);
        userEntityRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserEntity(String userName) {
        return userEntityRepository.findByUsername(userName);
    }

    public byte[] getURLAvatar(Integer id) {
        return avatarUserEntityRepository.findById(id).orElseThrow().getData();
    }
}
