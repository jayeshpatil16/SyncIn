package com.example.SyncIn.controller;

import com.example.SyncIn.dto.UpdatePasswordRequest;
import com.example.SyncIn.dto.UpdateProfileRequest;
import com.example.SyncIn.dto.UserProfileResponse;
import com.example.SyncIn.model.User;
import com.example.SyncIn.service.FollowService;
import com.example.SyncIn.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(
            new UserProfileResponse(
                    user.getUsername(),
                    user.getBio(),
                    user.getEmail(),
                    user.getFollowers(),
                    user.getFollowing(),
                    false,
                    false,
                    user.getAvatarUrl()
            )
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id, @AuthenticationPrincipal User user)
    {
        return userService.getProfile(id, user.getId());
    }

    @PutMapping("/profile/password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal User user, @RequestBody UpdatePasswordRequest updatePasswordRequest)
    {
        System.out.print(updatePasswordRequest);
        userService.updatePassword(user, updatePasswordRequest);
        return ResponseEntity.ok("Password changed successfully!!");
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequest updateProfileRequest)
    {
        return userService.updateUserProfile(user, updateProfileRequest);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal User user)
    {
        return userService.deleteProfile(user);
    }
}
