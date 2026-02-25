package com.example.SyncIn.controller;

import com.example.SyncIn.dto.CommentResponse;
import com.example.SyncIn.dto.CreateCommentRequest;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest createCommentRequest, @PathVariable Long id, @AuthenticationPrincipal User user)
    {
        return commentService.createComment(createCommentRequest, id, user);
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@RequestParam(required = false) String cursor, @RequestParam int limit, @PathVariable Long id)
    {
        return commentService.getComments(id, cursor, limit);
    }

    @GetMapping("/posts/{id}/replies")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id)
    {
        return commentService.getReplies(id);
    }
}
