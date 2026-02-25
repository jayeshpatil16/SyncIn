package com.example.SyncIn.service;

import com.example.SyncIn.dto.Author;
import com.example.SyncIn.dto.CommentResponse;
import com.example.SyncIn.dto.CreateCommentRequest;
import com.example.SyncIn.model.Comment;
import com.example.SyncIn.model.Post;
import com.example.SyncIn.model.User;
import com.example.SyncIn.repository.CommentRepository;
import com.example.SyncIn.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository)
    {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<CommentResponse> createComment(CreateCommentRequest createCommentRequest, Long postId, User user)
    {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found!"));
        Comment parentComment = null;
        if(createCommentRequest.getParentCommentId() != null) {
            parentComment = commentRepository.findById(createCommentRequest.getParentCommentId()).orElseThrow(() -> new RuntimeException("Comment not found"));
        }

        Comment comment = Comment.builder()
                .content(createCommentRequest.getContent()).parentComment(parentComment).post(post).user(user).build();
        commentRepository.save(comment);

        post.incrementCommentCount();
        postRepository.save(post);
        return ResponseEntity.ok(mapToCommentResponse(comment));
    }

    public ResponseEntity<List<CommentResponse>> getComments(Long postId, String cursor, int limit)
    {
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by("createdAt").descending()
        );

        LocalDateTime cursorTime = LocalDateTime.parse(cursor);

        List<Comment> comments;

        if(cursor == null)
        {
            comments = commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable);
        }
        else
        {
            comments = commentRepository.findByPostIdAndParentCommentIsNullAndCreatedAtBefore(postId, cursorTime, pageable);
        }

        return ResponseEntity.ok(comments.stream()
                .map(this::mapToCommentResponse).toList());
    }

    public ResponseEntity<List<CommentResponse>> getReplies(Long parentCommentId)
    {
        List<Comment> comments = commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentCommentId);

        return ResponseEntity.ok(comments.stream()
                .map(this::mapToCommentResponse).toList());
    }

    private CommentResponse mapToCommentResponse(Comment comment)
    {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                new Author(
                        comment.getUser().getAvatarUrl(),
                        comment.getUser().getUsername(),
                        comment.getUser().getId()
                ),
                comment.getUpVoteCount(),
                comment.getDownVoteCount()
        );
    }
}
