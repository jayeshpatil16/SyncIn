package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String username;
    private String bio;
    private String email;
    private int followers;
    private int following;
    private boolean followingMe;
    private boolean followedByMe;
    private String avatar_url;
}
