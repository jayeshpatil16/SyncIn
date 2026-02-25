package com.example.SyncIn.service;

import com.example.SyncIn.dto.*;
import com.example.SyncIn.model.Comment;
import com.example.SyncIn.model.Post;
import com.example.SyncIn.model.User;
import com.example.SyncIn.repository.CommentRepository;
import com.example.SyncIn.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository)
    {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<PostResponse> createPost(CreatePostRequest createPostRequest, User user)
    {
        Post post = Post.builder()
                .content(createPostRequest.getContent())
                .title(createPostRequest.getTitle())
                .user(user)
                .build();

        postRepository.save(post);

        return ResponseEntity.ok(mapToFeedResponse(post));
    }

    public ResponseEntity<PostResponse> editPost(Long postId, CreatePostRequest createPostRequest, User user)
    {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found!"));

        post.setContent(createPostRequest.getContent());
        post.setTitle(createPostRequest.getTitle());

        postRepository.save(post);

        return ResponseEntity.ok(mapToFeedResponse(post));
    }

    public List<PostResponse> getFeed(String cursor, int limit)
    {
        List<Post> posts;
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by("createdAt").descending()
        );

        if (cursor == null) {
            Page<Post> page = postRepository.findAll(pageable);
            posts = page.getContent();
        } else {
            LocalDateTime cursorTime = LocalDateTime.parse(cursor);
            posts = postRepository.findByCreatedAtBefore(cursorTime, pageable);
        }

        return posts.stream()
                .map(this::mapToFeedResponse)
                .toList();
    }

    public List<PostResponse> getMyFeed(String cursor, int limit, Long userId)
    {
        LocalDateTime cursorTime = LocalDateTime.parse(cursor);
        List<Post> posts;
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by("createdAt").descending()
        );

        if(cursor == null)
        {
            posts = postRepository.findByUserId(userId, pageable);
        }
        else
        {
            posts = postRepository.findByUserIdAndCreatedAtBefore(userId, cursorTime, pageable);
        }
        return posts.stream()
                .map(this::mapToFeedResponse)
                .toList();
    }

    public PostDetailResponse getDetailedPost(Long postId)
    {
        Post post = postRepository.findPostWithUserById(postId).orElseThrow(() -> new RuntimeException("Post not found!"));

        List<Comment> comments = commentRepository.findCommentsWithUserByPostId(postId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> new CommentResponse(
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
                )).toList();

        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                new Author(
                        post.getUser().getAvatarUrl(),
                        post.getUser().getUsername(),
                        post.getUser().getId()
                ),
                post.getUpVoteCount(),
                post.getDownVoteCount(),
                post.getCommentCount(),
                commentResponses
        );
    }

    public ResponseEntity<String> deletePost(Long userId)
    {
        Post post = postRepository.findById(userId).orElseThrow(() -> new RuntimeException("Post not found!"));
        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted!");
    }

    private PostResponse mapToFeedResponse(Post post)
    {
        return new PostResponse(
                post.getTitle(),
                post.getContent(),
                post.getId(),
                post.getCreatedAt(),
                new Author(
                        post.getUser().getAvatarUrl(),
                        post.getUser().getUsername(),
                        post.getUser().getId()
                ),
                post.getUpVoteCount(),
                post.getDownVoteCount(),
                post.getCommentCount()
        );
    }
}
