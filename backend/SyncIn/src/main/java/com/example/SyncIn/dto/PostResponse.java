package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponse {
    private String title;
    private String content;
    private Long id;
    private LocalDateTime createdAt;
    private Author author;
    private int upVoteCount;
    private int downVoteCount;
    private int commentCount;
}
