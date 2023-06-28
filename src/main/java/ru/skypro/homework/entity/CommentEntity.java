package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
public class CommentEntity {

    /**
     * Уникальный ID для хранения класса в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Данные автора комментария
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    /**
     * Время создания комментария
     */
    private LocalDateTime createTime;

    /**
     * Текст комментария
     */
    private String commentText;

    @ManyToOne
    @JoinColumn(name = "ads_id")
    private AdsEntity adsEntity;

}
