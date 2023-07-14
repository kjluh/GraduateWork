package ru.skypro.homework.entity;

import lombok.Data;
import ru.skypro.homework.dto.Role;
import javax.persistence.*;

@Entity
@Data
public class UserEntity {
    /**
     * Уникальный ID для хранения класса в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Роль пользователя
     */
    @Enumerated(EnumType.STRING)
    private Role role;
    /**
     * Логин пользователя
     */
    private String username;
    /**
     * Пароль пользователя
     */
    private String password;
    /**
     * Имя пользователя
     */
    private String firstName;
    /**
     * Фамилия пользователя
     */
    private String lastName;
    /**
     * Телефон пользователя
     */
    private String phone;
    /**
     * Аватар пользователя
     */
    @OneToOne
    private AvatarUserEntity avatarUserEntity;
}
