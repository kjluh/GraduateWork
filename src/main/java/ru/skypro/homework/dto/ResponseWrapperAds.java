package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapperAds {
    /**
     * Количество объявлений пользователя
     */
    private int count;
    /**
     * Коллекция объявлений пользователя
     */
    private Collection<Ads> results;
}
