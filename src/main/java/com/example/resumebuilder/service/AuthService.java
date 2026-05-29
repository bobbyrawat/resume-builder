package com.example.resumebuilder.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resumebuilder.document.User;
import com.example.resumebuilder.dto.AuthResponse;
import com.example.resumebuilder.dto.LoginRequest;
import com.example.resumebuilder.dto.RegisterRequest;
import com.example.resumebuilder.respository.UserRepository;
import com.example.resumebuilder.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // ================= REGISTER =================

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ENCODE PASSWORD
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        String token =
                jwtUtil.generateToken(user.getId());

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    // ================= LOGIN =================

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // CHECK HASHED PASSWORD
        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token =
                jwtUtil.generateToken(user.getId());

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

   //////////////////////////////////////////////////////
// VERIFY EMAIL
//////////////////////////////////////////////////////

public void verifyEmail(String token) {

    System.out.println(
            "Email verified with token: " + token
    );
}

//////////////////////////////////////////////////////
// RESEND VERIFICATION
//////////////////////////////////////////////////////

public void resendVerification(String email) {

    System.out.println(
            "Resend verification email to: " + email
    );
}


    // ================= PROFILE =================
public AuthResponse getProfile(Object principalObject) {

    String userId;

    // ✅ FIX: handle Spring Security properly
    if (principalObject instanceof org.springframework.security.core.userdetails.UserDetails) {
        userId = ((org.springframework.security.core.userdetails.UserDetails) principalObject).getUsername();
    }
    else if (principalObject instanceof String) {
        userId = (String) principalObject;
    }
    else {
        throw new RuntimeException(
                "Invalid authentication principal: " + principalObject
        );
    }

    User user =
            userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

    return AuthResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
}
 
}