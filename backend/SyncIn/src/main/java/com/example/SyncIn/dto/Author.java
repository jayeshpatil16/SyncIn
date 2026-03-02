package com.example.SyncIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author
{
    private String name;
    private String avatar_url;
    private String username;
    private Long id;
}
