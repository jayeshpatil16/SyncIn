package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CreateCommentRequest {
    private String content;
    private Long parentCommentId;
}
