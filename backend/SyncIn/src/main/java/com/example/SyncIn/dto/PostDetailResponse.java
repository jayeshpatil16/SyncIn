package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Author author;
    private int upVoteCount;
    private int downVoteCount;
    private int commentCount;
    private List<CommentResponse> comments;
}
