package com.example.resumebuilder.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumebuilder.service.TemplatesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/templates")
@Slf4j
public class TemplatesController {
      

    private final TemplatesService templatesService;




    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication) {
        // Call the Service method
        Map<String, Object> response = templatesService.getTemplates(authentication.getPrincipal());

        // Return the Response
        return ResponseEntity.ok(response);
    }

    
}
