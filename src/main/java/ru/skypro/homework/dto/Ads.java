package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ads {
    /**
     * Id автора объявления
     */
    private int author;
    /**
     * Ссылка на картинку объявления
     */
    private String image;
    /**
     * Id объявления
     */
    private int pk;
    /**
     * Цена товара в объявлении
     */
    private int price;
    /**
     * Заголовок в объявлении
     */
    private String title;
}
