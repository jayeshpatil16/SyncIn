package com.example.SyncIn.repository;

import com.example.SyncIn.model.User;
import com.example.SyncIn.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);

    Optional<Vote> findByUserIdAndCommentId(Long userId, Long commentId);

    Long user(User user);
}
