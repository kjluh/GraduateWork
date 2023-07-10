package ru.skypro.homework.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Getter
@Entity
public class AdsImage {
    /**
     * Уникальный ID для хранения аватара в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Размер аватар объявления
     */
    private long fileSize;
    /**
     * Адрес где лежит аватар объявления
     */
    private String filePath;
    /**
     * Тип аватарки
     */
    private String contentType;
    /**
     * Объявление к которому относится аватар
     */
    @OneToOne
    private AdsEntity ads;
}
