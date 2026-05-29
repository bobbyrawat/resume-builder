package com.example.resumebuilder.dto;

import java.time.LocalDateTime;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
 
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    private String subscriptionPlan;
    private String emailVerified;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    
}
