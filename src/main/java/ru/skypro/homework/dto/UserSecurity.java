package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserSecurity {
    /**
     * ID пользователя
     */
    private int id;
    /**
     * Логин пользователя
     */
    private String login;
    /**
     * Пароль пользователя (зашифрованный)
     */
    private String password;
    /**
     * Роль пользователя
     */
    private Role role;
}
