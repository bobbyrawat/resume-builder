package com.example.resumebuilder.document;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
     @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String profileImageUrl;
    @Builder.Default    
    private String subscriptionPlan = "basic";
    @Builder.Default
    private boolean emailVerified =  false;
    private String verificationToken;
    private LocalDateTime verificationExpires;
     
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    
    
}
