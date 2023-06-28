package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateComment {

    private String text;

    @Override
    public String toString() {
        return text;
    }
}
