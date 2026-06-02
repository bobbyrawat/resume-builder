 package com.example.resumebuilder.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    // TEMP SIMPLE CREATE RESUME (you can connect service later)
    @PostMapping
    public Map<String, Object> createResume(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> request
    ) {

        // just log token for now
        System.out.println("Auth Header: " + authHeader);
        System.out.println("Request Body: " + request);

        Map<String, Object> response = new HashMap<>();

        response.put("id", UUID.randomUUID().toString());
        response.put("title", request.get("title"));
        response.put("message", "Resume created successfully");

        return response;
    }
}