package com.example.resumebuilder.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.resumebuilder.dto.AuthResponse;
import com.example.resumebuilder.dto.LoginRequest;
import com.example.resumebuilder.dto.RegisterRequest;
import com.example.resumebuilder.service.AuthService;
import com.example.resumebuilder.service.FileUploadService;

import static com.example.resumebuilder.util.AppConstants.AUTH_CONTROLLER;
import static com.example.resumebuilder.util.AppConstants.LOGIN;
import static com.example.resumebuilder.util.AppConstants.PROFILE;
import static com.example.resumebuilder.util.AppConstants.REGISTER;
import static com.example.resumebuilder.util.AppConstants.RESEND_VERIFICATION;
import static com.example.resumebuilder.util.AppConstants.UPLOAD_PROFILE;
import static com.example.resumebuilder.util.AppConstants.VERIFY_EMAIL;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;

    private final FileUploadService fileUploadService;

    //////////////////////////////////////////////////////
    // REGISTER
    //////////////////////////////////////////////////////

    @PostMapping(REGISTER)
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        AuthResponse response =
                authService.register(request);

        log.info("User registered successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //////////////////////////////////////////////////////
    // LOGIN
    //////////////////////////////////////////////////////

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    //////////////////////////////////////////////////////
    // VERIFY EMAIL
    //////////////////////////////////////////////////////

    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token
    ) {

        log.info(
                "Inside AuthController - verifyEmail(): {}",
                token
        );

        authService.verifyEmail(token);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Email verified successfully"
                )
        );
    }

    //////////////////////////////////////////////////////
    // RESEND VERIFICATION
    //////////////////////////////////////////////////////

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(
            @RequestBody Map<String, String> body
    ) {

        String email = body.get("email");

        if (Objects.isNull(email) || email.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Email is required"
                    )
            );
        }

        authService.resendVerification(email);

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,

                        "message",
                        "Verification email sent"
                )
        );
    }

    //////////////////////////////////////////////////////
    // UPLOAD PROFILE IMAGE
    //////////////////////////////////////////////////////

    @PostMapping(UPLOAD_PROFILE)
    public ResponseEntity<?> uploadImage(
            @RequestPart("image") MultipartFile file
    ) throws IOException {

        log.info(
                "Inside AuthController - uploadImage()"
        );

        Map<String, String> response =
                fileUploadService.uploadSingleImage(file);

        return ResponseEntity.ok(response);
    }

    //////////////////////////////////////////////////////
    // GET PROFILE
    //////////////////////////////////////////////////////

    @GetMapping(PROFILE)
    public ResponseEntity<?> getProfile(
            Authentication authentication
    ) {

        //////////////////////////////////////////////////////
        // GET USER ID FROM JWT AUTH
        //////////////////////////////////////////////////////

        String userId = authentication.getName();

        //////////////////////////////////////////////////////
        // FETCH PROFILE
        //////////////////////////////////////////////////////

        AuthResponse currentProfile =
                authService.getProfile(userId);

        return ResponseEntity.ok(currentProfile);
    }
}