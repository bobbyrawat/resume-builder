package com.example.resumebuilder.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.resumebuilder.document.Resume;
import com.example.resumebuilder.respository.ResumeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    // CREATE
    public Resume createResume(String title, String userId) {

        Resume newResume = new Resume();

        newResume.setUserId(userId);
        newResume.setTitle(title);

        newResume.setTemplate("template1");
        newResume.setIsPremium(false);

        setDefaultResumeData(newResume);

        return resumeRepository.save(newResume);
    }

    // GET
    public Resume getResumeById(String id, String userId) {

        Resume resume = resumeRepository
                .findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        setDefaultResumeData(resume);

        return resume;
    }

    // UPDATE
    public Resume updateResume(String id, Resume updated, String userId) {

        Resume existing = resumeRepository
                .findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle());
        }

        if (updated.getTemplate() != null) {
            existing.setTemplate(updated.getTemplate());
        }

        if (updated.getProfileInfo() != null) {
            existing.setProfileInfo(updated.getProfileInfo());
        }

        if (updated.getContactInfo() != null) {
            existing.setContactInfo(updated.getContactInfo());
        }

        if (updated.getWorkExperiences() != null) {
            existing.setWorkExperiences(updated.getWorkExperiences());
        }

        if (updated.getSkills() != null) {
            existing.setSkills(updated.getSkills());
        }

        if (updated.getProjects() != null) {
            existing.setProjects(updated.getProjects());
        }

        if (updated.getLanguages() != null) {
            existing.setLanguages(updated.getLanguages());
        }

        if (updated.getCustomSections() != null) {
            existing.setCustomSections(updated.getCustomSections());
        }

        return resumeRepository.save(existing);
    }

    // DELETE
    public void deleteResume(String id, String userId) {

        Resume resume = resumeRepository
                .findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resumeRepository.delete(resume);
    }

    // DEFAULT DATA
    private void setDefaultResumeData(Resume r) {

        if (r.getTemplate() == null) r.setTemplate("template1");

        if (r.getSkills() == null) r.setSkills(new ArrayList<>());
        if (r.getProjects() == null) r.setProjects(new ArrayList<>());
        if (r.getLanguages() == null) r.setLanguages(new ArrayList<>());
        if (r.getCustomSections() == null) r.setCustomSections(new ArrayList<>());
    }
}