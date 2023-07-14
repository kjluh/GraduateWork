package ru.skypro.homework.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Data
public class AdsEntity {
    /**
     * Уникальный ID для хранения класса в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Автор объявления
     */
    @ManyToOne
    private UserEntity author;
    /**
     * Заголовок объявления
     */
    private String title;
    /**
     * Текст объявления
     */
    private String description;
    /**
     * Стоимость товара в объявлении
     */
    private int price;
    /**
     * Врем создания объявления
     */
    private LocalDateTime createdAt;
}
