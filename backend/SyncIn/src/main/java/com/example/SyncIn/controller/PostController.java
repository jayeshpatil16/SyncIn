package com.example.SyncIn.controller;

import com.example.SyncIn.dto.CreatePostRequest;
import com.example.SyncIn.dto.PostDetailResponse;
import com.example.SyncIn.dto.PostResponse;
import com.example.SyncIn.model.Post;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService)
    {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal User user, @RequestBody CreatePostRequest createPostRequest)
    {
        return postService.createPost(createPostRequest, user);
    }

    @GetMapping("/feed/")
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam(required = false) String cursor, @RequestParam(defaultValue = "10") int limit)
    {
        List<PostResponse> posts = postService.getFeed(cursor, limit);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed/posts/{id}")
    public ResponseEntity<PostDetailResponse> getDetailedPost(@PathVariable Long id)
    {
        PostDetailResponse postDetailResponse = postService.getDetailedPost(id);
        return ResponseEntity.ok(postDetailResponse);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<List<PostResponse>> getMyPosts(@RequestParam(required = false) String cursor, @RequestParam int limit, @AuthenticationPrincipal User user)
    {
        List<PostResponse> myPosts = postService.getMyFeed(cursor, limit, user.getId());

        return ResponseEntity.ok(myPosts);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponse> editPost(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody CreatePostRequest createPostRequest)
    {
        return postService.editPost(id, createPostRequest, user);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id)
    {
        return postService.deletePost(id);
    }
}
