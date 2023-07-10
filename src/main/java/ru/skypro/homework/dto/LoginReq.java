package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class LoginReq {
    /**
     * Пароль пользователя
     */
    private String password;
    /**
     * Логин пользователя
     */
    private String username;

}