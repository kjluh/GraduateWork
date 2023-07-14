package ru.skypro.homework.dto;
import lombok.Data;

import javax.persistence.*;

/**
 * Сущность для хранения объявления
 */
@Data
public class FullAds {
    /**
     * ID объявления
     */
    private int pk;
    /**
     * Имя автора объявления
     */
    private String authorFirstName;
    /**
     * Фамилия автора объявления
     */
    private String authorLastName;
    /**
     * Текст объявления
     */
    private String description;
    /**
     * Логин автора объявления
     */
    private String email;
    /**
     * Аватарка объявления
     */
    private String image;
    /**
     * Телефон автора объявления
     */
    private String phone;
    /**
     * Цена объявления
     */
    private int price;
    /**
     * Заголовок объявления
     */
    private String title;

}
