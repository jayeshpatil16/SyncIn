package com.example.SyncIn.service;

import com.example.SyncIn.CustomException.UserAlreadyExistsException;
import com.example.SyncIn.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SyncIn.dto.RegisterRequest;
import com.example.SyncIn.repository.UserRepository;
import com.example.SyncIn.dto.LoginRequest;

import com.example.SyncIn.model.User;


@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest registerRequest)
    {
        if(userRepository.existsByEmail(registerRequest.getEmail()))
        {
            throw new UserAlreadyExistsException("User with this email already exists", "email");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername()))
        {
            throw new UserAlreadyExistsException("User with this username already exists", "username");
        }

        User user = User.builder()
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
            .build();

        userRepository.save(user);

        System.out.print("User registered successfully");

        return "User registered successfully!!";
    }

    public LoginResponse login(LoginRequest loginRequest)
    {
        User user = userRepository.findByEmailOrUsername(loginRequest.getIdentifier(), loginRequest.getIdentifier())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash()))
        {
            throw new RuntimeException("Invalid credentials");
        };

        String token = jwtService.generateToken(user.getId().toString());

        LoginResponse loginResponse = new LoginResponse(
                token,
                user.getName(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl()
        );

        return loginResponse;
    }
}
