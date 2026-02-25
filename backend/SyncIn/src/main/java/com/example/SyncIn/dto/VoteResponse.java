package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResponse {
    private int upVoteCount;
    private int downVoteCount;
    private String userVote;
}
