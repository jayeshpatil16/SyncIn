package com.example.SyncIn.model;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(
    name = "votes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
    },
    indexes = {
        @Index(name = "idx_vote_post", columnList = "post_id"),
        @Index(name = "idx_vote_comment", columnList = "comment_id"),
        @Index(name = "idx_vote_user", columnList = "user_id")
    }
)
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Setter
    @Getter
    @Column(nullable = false)
    private VoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private LocalDateTime votedOn;

    @PrePersist
    protected void onCreate()
    {
        this.votedOn = LocalDateTime.now();
    }


    public boolean isSameType(VoteType incomingVoteType)
    {
        return this.voteType == incomingVoteType;
    }
}
