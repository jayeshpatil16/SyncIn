package com.example.SyncIn.dto;

import lombok.*;

@Data
public class LoginRequest {
    private String identifier;
    private String password;
}
