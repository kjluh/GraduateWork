package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class NewPassword {
    /**
     * Старый пароль
     */
    private String currentPassword;
    /**
     * Новый пароль
     */
    private String newPassword;
}
