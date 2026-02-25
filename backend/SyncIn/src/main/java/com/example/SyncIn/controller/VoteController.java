package com.example.SyncIn.controller;

import com.example.SyncIn.dto.CreateVoteRequest;
import com.example.SyncIn.dto.VoteResponse;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService)
    {
        this.voteService = voteService;
    }

    @PostMapping("/posts/{id}/vote")
    public ResponseEntity<VoteResponse> votePost(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody CreateVoteRequest createVoteRequest)
    {
        return voteService.votePost(id, user, createVoteRequest.getVoteType());
    }

    @PostMapping("/comments/{id}/vote")
    public ResponseEntity<VoteResponse> voteComment(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody CreateVoteRequest createVoteRequest)
    {
        return voteService.voteComment(id, user, createVoteRequest.getVoteType());
    }
}
