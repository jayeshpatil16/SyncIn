package com.example.SyncIn.controller;

import com.example.SyncIn.dto.FollowListResponse;
import com.example.SyncIn.dto.FollowResponse;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService)
    {
        this.followService = followService;
    }

    @PostMapping("/user/{id}/follow")
    public ResponseEntity<FollowResponse> followUser(@PathVariable Long id, @AuthenticationPrincipal User user)
    {
        return followService.followUser(user, id);
    }

    @GetMapping("/users/{id}/followers")
    public ResponseEntity<List<FollowListResponse>> getFollowers(@PathVariable Long id)
    {
        return followService.getFollowers(id);
    }

    @GetMapping("/users/{id}/following")
    public ResponseEntity<List<FollowListResponse>> getFollowing(@PathVariable Long id)
    {
        return followService.getFollowing(id);
    }

    @DeleteMapping("/user/{id}/unfollow")
    public ResponseEntity<FollowResponse> unFollowUser(@PathVariable Long id, @AuthenticationPrincipal User user)
    {
        System.out.print("Reached here mission yet to be complete");
        return followService.unfollowUser(user, id);
    }
}
