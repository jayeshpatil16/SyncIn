package com.example.SyncIn.repository;

import com.example.SyncIn.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findCommentsWithUserByPostId(@Param("postId") Long postId);

    List<Comment> findByPostIdAndParentCommentIsNullAndCreatedAtBefore(
            Long postId,
            LocalDateTime cursor,
            Pageable pageable
    );

    List<Comment> findByPostIdAndParentCommentIsNull(
            Long postId,
            Pageable pageable
    );

    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long commentId);
}
