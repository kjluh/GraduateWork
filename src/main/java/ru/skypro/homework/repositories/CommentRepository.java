package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {

    List<CommentEntity> findCommentEntitiesByAdsEntity_Id(int id);

    CommentEntity findCommentEntityByAdsEntity_Id(int adId);


    void deleteCommentEntityByAdsEntity_IdAndId(int adId, int id);

    void deleteAllByAdsEntity_Id(int adId);

}
