package com.example.SyncIn.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String passwordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes;

    @Builder.Default
    private int followers = 0;

    @Builder.Default
    private int following = 0;

    private String avatarUrl;

    private String bio;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }



    public void increaseFollowers()
    {
        followers++;
    }

    public void increaseFollowing()
    {
        following++;
    }

    public void decreaseFollowing()
    {
        if(this.following > 0)
            following--;
    }

    public void decreaseFollowers()
    {
        if(this.followers > 0)
            followers--;
    }
}
