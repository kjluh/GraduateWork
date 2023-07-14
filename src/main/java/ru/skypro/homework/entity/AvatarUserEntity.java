package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AvatarUserEntity {
    /**
     * Уникальный ID для хранения аватара в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Тип аватарки
     */
    private String mediaType;
    /**
     * Адрес где лежит аватар
     */
    private String filePath;
    /**
     * Аватар в массиве байт
     */
    @Lob
    private byte[] data;
    /**
     * Владелец аватара
     */
    @OneToOne(mappedBy = "avatarUserEntity")
    private UserEntity userEntity;
}
