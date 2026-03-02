package com.example.SyncIn.repository;

import com.example.SyncIn.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.following.id = :id")
    List<Follow> findFollowersWithUser(@Param("id") Long id);

    @Query("SELECT f FROM Follow f JOIN FETCH f.following WHERE f.follower.id = :id")
    List<Follow> findFollowingWithUser(@Param("id") Long id);
}
