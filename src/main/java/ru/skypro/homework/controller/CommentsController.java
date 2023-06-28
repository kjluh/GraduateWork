package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateComment;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getCom(@PathVariable int id) {
        return ResponseEntity.ok(commentService.getAllCommentsByAdsId(id));
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> saveCom(@PathVariable int id, @RequestBody CreateComment text) {
        return ResponseEntity.ok(commentService.createNewAdsComment(id, text));
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteCom(@PathVariable int adId, @PathVariable int commentId) {
        commentService.deleteCommentByAdsIdAndCommentEntityId(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateCom(@PathVariable int adId, @PathVariable int commentId, @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.patchCommentByAdsIdAndCommentEntityId(adId, commentId, comment));
    }
}
