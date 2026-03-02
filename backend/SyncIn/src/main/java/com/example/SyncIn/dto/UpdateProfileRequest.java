package com.example.SyncIn.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String name;
    private String bio;
    private String email;
    private String avatar_url;
}