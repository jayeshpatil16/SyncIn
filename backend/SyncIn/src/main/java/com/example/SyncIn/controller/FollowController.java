package com.example.SyncIn.controller;

import com.example.SyncIn.dto.FollowResponse;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/user/{id}/unfollow")
    public ResponseEntity<String> unFollowUser(@PathVariable Long id, @AuthenticationPrincipal User user)
    {
        return followService.unfollowUser(user, id);
    }
}
