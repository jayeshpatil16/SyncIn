package com.example.SyncIn.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "posts",
    indexes = {
        @Index(name = "idx_post_user", columnList = "user_id"),
        @Index(name = "idx_post_created", columnList = "createdAt")
    }
)
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @Column(nullable = false, length = 200)
    private String title;

    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comment;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes;

    private LocalDateTime createdAt;

    private int upVoteCount;

    private int downVoteCount;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }

    public void upVoteIncrease()
    {
        upVoteCount++;
    }

    public void upVoteDecrease()
    {
        if(this.upVoteCount > 0)
            upVoteCount--;
    }

    public void downVoteDecrease()
    {
        if(this.downVoteCount > 0)
            downVoteCount--;
    }


    public void downVoteIncrease()
    {
        downVoteCount++;
    }
}
