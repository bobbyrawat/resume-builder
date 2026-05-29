package com.example.resumebuilder.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    //////////////////////////////////////////////////////
    // CREATE RESUME
    //////////////////////////////////////////////////////

    @PostMapping
    public ResponseEntity<?> createResume(
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {

        Resume res = resumeService.createResume(
                body.get("title"),
                auth.getPrincipal()
        );

        return ResponseEntity.ok(res);
    }

    //////////////////////////////////////////////////////
    // GET RESUME
    //////////////////////////////////////////////////////

    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(
            @PathVariable String id,
            Authentication auth
    ) {

        try {

            Resume res = resumeService.getResumeById(
                    id,
                    auth.getPrincipal()
            );

            return ResponseEntity.ok(res);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(500).body(
                    "ERROR: " + e.getMessage()
            );
        }
    }

    //////////////////////////////////////////////////////
    // UPDATE RESUME
    //////////////////////////////////////////////////////

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(
            @PathVariable String id,
            @RequestBody Resume data,
            Authentication auth
    ) {

        Resume updated = resumeService.updateResume(
                id,
                data,
                auth.getPrincipal()
        );

        return ResponseEntity.ok(updated);
    }

    //////////////////////////////////////////////////////
    // DELETE RESUME
    //////////////////////////////////////////////////////

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(
            @PathVariable String id,
            Authentication auth
    ) {

        resumeService.deleteResume(
                id,
                auth.getPrincipal()
        );

        return ResponseEntity.ok(
                Map.of("message", "deleted")
        );
    }
}