package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapperComment {
    /**
     * Количество комментариев
     */
    private int count;
    /**
     * Массив всех комментариев объявления
     */
    private Collection<Comment> results;
}
