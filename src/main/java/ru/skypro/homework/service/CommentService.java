package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateComment;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.security.MyUserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    @Lazy
    @Autowired
    private AdsService adsService;
    private final MyUserDetails myUserDetails; // спринг секьюрити сюда положит авторизированного пользователя


    public CommentService(CommentRepository commentsForAdsRepository, UserService userService, MyUserDetails myUserDetails) {
        this.commentRepository = commentsForAdsRepository;
        this.userService = userService;
        this.myUserDetails = myUserDetails;
    }

    /**
     * Вернуть все комментарии выбранного объявления
     * @param adId уникальный ID объявления
     * @return ДТО обертка для коллекции комментариев
     */
    @Transactional(readOnly = true)
    public ResponseWrapperComment getAllCommentsByAdsId(int adId) {
        List<CommentEntity> commentEntityList = new ArrayList<>();
        commentEntityList.addAll(commentRepository.findCommentEntitiesByAdsEntity_Id(adId));

        List<Comment> commentDtoList = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntityList) {
            Comment thisComment = CommentMapper.INSTANCE.commentEntityToComment(commentEntity);
//            если аватарки нет ссылку не сетим
            if (commentEntity.getAuthor().getAvatarUserEntity() != null) {
                thisComment.setAuthorImage("/users/avatar/" + userService.getUserEntity(commentEntity.getAuthor().getUsername()).getId() + "/db");
            }
            //При мапинге время не переделывалось строку.
            thisComment.setCreatedAt(commentEntity.getCreateTime().toInstant(ZoneOffset.UTC).toEpochMilli());

            commentDtoList.add(thisComment);
        }

        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setCount(commentDtoList.size());
        responseWrapperComment.setResults(commentDtoList);
        return responseWrapperComment;
    }

    /**
     * Сохранить новый комментарий к объявлению
     * @param adId уникальный ID объявления
     * @param commentText текст комментария
     * @return ДТО комментария
     */
    @Transactional
    public Comment createNewAdsComment(int adId, CreateComment commentText) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentText(commentText.toString());
        commentEntity.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        commentEntity.setAuthor(userService.getUserEntity(myUserDetails.getUsername())); // чуть переделал авторизацию
        commentEntity.setAdsEntity(adsService.findById(adId));
        commentRepository.save(commentEntity);

        Comment newComment = CommentMapper.INSTANCE.commentEntityToComment(commentEntity);
        newComment.setCreatedAt(commentEntity.getCreateTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        return newComment;
    }

    /**
     * Удалить комментарий у объявления
     * @param adId уникальный ID объявления
     * @param id уникальный ID комментария
     * @return ДТО комментария
     */
    @Transactional
    public boolean deleteCommentByAdsIdAndCommentEntityId(int adId, int id) {
        if (isChoiceRole(id)) {
            commentRepository.deleteCommentEntityByAdsEntity_IdAndId(adId, id);
            return true;
        }
        return false;
    }

    /**
     * Удалить все комментарии у объявления
     * @param adId уникальный ID объявления
     */
    @Transactional
    public void deleteAllCommentsByAdsId (int adId) {
        commentRepository.deleteAllByAdsEntity_Id(adId);
    }

    /**
     * Обновить комментарий у объявления
     * @param adId уникальный ID объявления
     * @param id уникальный ID комментария
     * @param comment новый комментарий
     * @return ДТО комментария
     */
    @Transactional
    public Comment patchCommentByAdsIdAndCommentEntityId(int adId, int id, Comment comment) {
        if (isChoiceRole(id)) {
            CommentEntity commentEntityTest = commentRepository.findById(id).orElseThrow();
            comment.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli());
            commentEntityTest.setCreateTime(Instant.ofEpochMilli(comment.getCreatedAt()).atZone(ZoneOffset.UTC).toLocalDateTime());
            commentEntityTest.setAdsEntity(adsService.findById(adId));
            commentEntityTest.setCommentText(comment.getText());
            commentRepository.save(commentEntityTest);
            return comment;
        }
        return null;
    }

    /**
     * Проверка принадлежности объявления авторизованному пользователю
     * @param id
     * @return
     */
    public boolean isChoiceRole(int id) {
        return (myUserDetails.getUsername().equals(commentRepository.findById(id).orElseThrow().getAuthor().getUsername())
                || myUserDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains("ROLE_ADMIN"));
    }

}


