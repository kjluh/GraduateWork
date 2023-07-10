package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateComment {
    /**
     * Текст объявления
     */
    private String text;

    @Override
    public String toString() {
        return text;
    }
}
