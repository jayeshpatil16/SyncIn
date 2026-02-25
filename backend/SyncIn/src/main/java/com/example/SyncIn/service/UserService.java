package com.example.SyncIn.service;


import com.example.SyncIn.dto.UpdatePasswordRequest;
import com.example.SyncIn.dto.UpdateProfileRequest;
import com.example.SyncIn.dto.UserProfileResponse;
import com.example.SyncIn.model.User;
import com.example.SyncIn.repository.FollowRepository;
import com.example.SyncIn.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
    }

    public ResponseEntity<UserProfileResponse> getProfile(Long targetId, Long userId)
    {
        User user = userRepository.findById(targetId).orElseThrow(() -> new RuntimeException("User not found"));


        return ResponseEntity.ok(
                new UserProfileResponse(
                        user.getUsername(),
                        user.getBio(),
                        user.getEmail(),
                        user.getFollowers(),
                        user.getFollowing(),
                        followRepository.existsByFollowerIdAndFollowingId(targetId, userId),
                        followRepository.existsByFollowerIdAndFollowingId(userId, targetId),
                        user.getAvatarUrl()
                )
        );
    }

    public void updatePassword(User user, UpdatePasswordRequest updatePasswordRequest)
    {
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPasswordHash()))
        {
            throw new RuntimeException("Current password is incorrect!");
        }

        user.changePasswordHash(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public ResponseEntity<UserProfileResponse> updateUserProfile(User user, UpdateProfileRequest updateProfileRequest)
    {
        user.setEmail(updateProfileRequest.getEmail());
        user.setUsername(updateProfileRequest.getUsername());
        user.setBio(updateProfileRequest.getBio());
        user.setAvatarUrl(updateProfileRequest.getAvatar_url());

        userRepository.save(user);

        return ResponseEntity.ok(new UserProfileResponse(
                user.getUsername(),
                user.getBio(),
                user.getEmail(),
                user.getFollowers(),
                user.getFollowing(),
                false,
                false,
                user.getAvatarUrl()
        ));
    }

    public ResponseEntity<String> deleteProfile(User user)
    {
        userRepository.delete(user);
        return ResponseEntity.ok("Profile deleted successfully!");
    }
}
