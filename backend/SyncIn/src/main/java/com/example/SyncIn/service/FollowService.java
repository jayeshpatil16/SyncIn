package com.example.SyncIn.service;

import com.example.SyncIn.dto.FollowResponse;
import com.example.SyncIn.model.Follow;
import com.example.SyncIn.model.User;
import com.example.SyncIn.repository.FollowRepository;
import com.example.SyncIn.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository)
    {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<FollowResponse> followUser(User user, Long followingId)
    {
        if(user.getId().equals(followingId))
            throw new RuntimeException("Can't follow yourself");

        if(followRepository.existsByFollowerIdAndFollowingId(user.getId(), followingId))
        {
            throw new RuntimeException("User already follows!!");
        }
        user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found!"));
        User followingUser = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("User does not exists!"));

        Follow follow = Follow.builder()
                .follower(user)
                .following(followingUser)
                .build();

        user.increaseFollowing();
        followingUser.increaseFollowers();

        followRepository.save(follow);

        return ResponseEntity.ok(
                new FollowResponse(
                        true,
                        followingUser.getFollowers(),
                        followingUser.getFollowing()
                )
        );
    }

    public ResponseEntity<String> unfollowUser(User user, Long followingId)
    {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(user.getId(), followingId).orElseThrow(() -> new RuntimeException("You don't follow each other"));
        User followingUser = follow.getFollowing();
        user = follow.getFollower();
        user.decreaseFollowing();
        followingUser.decreaseFollowers();

        followRepository.delete(follow);

        return ResponseEntity.ok("User unfollowed");
    }
}