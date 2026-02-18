package com.example.SyncIn.model;
import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(
    name = "comments",
    indexes = {
        @Index(name = "idx_comment_post_created", columnList = "post_id, createdAt"),
        @Index(name = "idx_comment_parent", columnList = "parent_comment_id")
    }
)
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
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
