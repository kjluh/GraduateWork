package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateAds {
    /**
     * Текст объявления
     */
    private String description;
    /**
     * Стоимость товара в объявлении
     */
    private int price;
    /**
     * Заголовок объявления
     */
    private String title;
}
