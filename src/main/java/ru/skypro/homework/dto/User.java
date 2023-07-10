package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Пользователь DTO
 */
@Data
public class User {
    /**
     * ID пользователя
     */
    private int id;
    /**
     * Логин пользователя
     */
    private String email;
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
     * Ссылка на аватар пользователя
     */
    private String image;
}
