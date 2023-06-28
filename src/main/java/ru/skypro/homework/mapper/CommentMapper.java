package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.avatarUserEntity.filePath", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "commentText", target = "text")
//    @Mapping(source = "createTime", target = "createdAt")
    Comment commentEntityToComment(CommentEntity commentEntity);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "authorImage", target = "author.avatarUserEntity.filePath")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    @Mapping(source = "text", target = "commentText")
    CommentEntity commentToCommentEntity (Comment comment);




}
