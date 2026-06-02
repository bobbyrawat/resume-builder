package com.example.resumebuilder.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumebuilder.document.Resume;
import com.example.resumebuilder.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3000",
        "https://resume-builder-frontend-navy-seven.vercel.app"
})
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createResume(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal String userId
    ) {
        String title = (String) body.getOrDefault("title", "My Resume");

        Resume resume = resumeService.createResume(title, userId);

        return ResponseEntity.ok(resume);
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(
            @PathVariable String id,
            @AuthenticationPrincipal String userId
    ) {
        Resume resume = resumeService.getResumeById(id, userId);
        return ResponseEntity.ok(resume);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(
            @PathVariable String id,
            @RequestBody Resume body,
            @AuthenticationPrincipal String userId
    ) {
        Resume updated = resumeService.updateResume(id, body, userId);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(
            @PathVariable String id,
            @AuthenticationPrincipal String userId
    ) {
        resumeService.deleteResume(id, userId);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}