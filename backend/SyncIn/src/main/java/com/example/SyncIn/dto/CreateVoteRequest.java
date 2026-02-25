package com.example.SyncIn.dto;

import com.example.SyncIn.model.VoteType;
import lombok.Data;

@Data
public class CreateVoteRequest {
    private VoteType voteType;
}
