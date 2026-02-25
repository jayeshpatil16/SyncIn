package com.example.SyncIn.repository;

import com.example.SyncIn.model.Post;
import com.example.SyncIn.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    List<Post> findByCreatedAtBefore(
            LocalDateTime cursor,
            Pageable pageable
    );

    Page<Post> findAll(Pageable pageable);

    List<Post> findByUserId(Long userId, Pageable pageable);

    List<Post> findByUserIdAndCreatedAtBefore(
            Long userId,
            LocalDateTime cursor,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findPostWithUserById(@Param("id") Long id);

    Long user(User user);
}
