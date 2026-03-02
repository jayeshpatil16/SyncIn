package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowListResponse {
    private Long id;
    private String name;
    private String avatar_url;
    private String username;
}
