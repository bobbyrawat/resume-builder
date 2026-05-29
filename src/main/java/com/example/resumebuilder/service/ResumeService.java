package com.example.resumebuilder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.resumebuilder.document.Resume;
import com.example.resumebuilder.dto.AuthResponse;
import com.example.resumebuilder.respository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    //////////////////////////////////////////////////
    // CREATE RESUME
    //////////////////////////////////////////////////

    public Resume createResume(String title, Object principalObject) {

        AuthResponse response =
                authService.getProfile(principalObject);

        Resume newResume = new Resume();

        newResume.setUserId(response.getId());
        newResume.setTitle(title);

        // DEFAULT TEMPLATE
        newResume.setTemplate("template1");
        newResume.setIsPremium(false);

        // SET DEFAULT SAFE DATA
        setDefaultResumeData(newResume);

        return resumeRepository.save(newResume);
    }

    //////////////////////////////////////////////////
    // DEFAULT DATA
    //////////////////////////////////////////////////

    private void setDefaultResumeData(Resume newResume) {

        if (newResume.getTemplate() == null) {
            newResume.setTemplate("template1");
        }

        if (newResume.getProfileInfo() == null) {
            newResume.setProfileInfo(new Resume.ProfileInfo());
        }

        if (newResume.getContactInfo() == null) {
            newResume.setContactInfo(new Resume.ContactInfo());
        }

        if (newResume.getWorkExperiences() == null) {
            newResume.setWorkExperiences(new ArrayList<>());
        }

        if (newResume.getEducation() == null) {
            newResume.setEducation(new ArrayList<>());
        }

        if (newResume.getSkills() == null) {
            newResume.setSkills(new ArrayList<>());
        }

        if (newResume.getProjects() == null) {
            newResume.setProjects(new ArrayList<>());
        }

        if (newResume.getCertifications() == null) {
            newResume.setCertifications(new ArrayList<>());
        }

        if (newResume.getLanguages() == null) {
            newResume.setLanguages(new ArrayList<>());
        }

        if (newResume.getInterests() == null) {
            newResume.setInterests(new ArrayList<>());
        }


if (newResume.getCustomSections() == null) {
    newResume.setCustomSections(new ArrayList<>());
}
    }

    //////////////////////////////////////////////////
    // GET USER RESUMES
    //////////////////////////////////////////////////

    public List<Resume> getUserResumes(Object principal) {

        AuthResponse response =
                authService.getProfile(principal);

        return resumeRepository
                .findByUserIdOrderByUpdatedAtDesc(
                        response.getId()
                );
    }

    //////////////////////////////////////////////////
    // GET RESUME BY ID
    //////////////////////////////////////////////////

    public Resume getResumeById(
            String id,
            Object principal
    ) {

        AuthResponse response =
                authService.getProfile(principal);

        Resume resume =
                resumeRepository
                        .findByUserIdAndId(
                                response.getId(),
                                id
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        // MAKE EVERYTHING SAFE
        setDefaultResumeData(resume);

        return resume;
    }

    //////////////////////////////////////////////////
    // UPDATE RESUME
    //////////////////////////////////////////////////

    public Resume updateResume(
            String id,
            Resume updatedData,
            Object principal
    ) {

        AuthResponse response =
                authService.getProfile(principal);

        Resume existing =
                resumeRepository
                        .findByUserIdAndId(
                                response.getId(),
                                id
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        //////////////////////////////////////////////////
        // BASIC DETAILS
        //////////////////////////////////////////////////

        if (updatedData.getTitle() != null) {
            existing.setTitle(updatedData.getTitle());
        }

        //////////////////////////////////////////////////
        // TEMPLATE
        //////////////////////////////////////////////////

        if (updatedData.getTemplate() != null) {
            existing.setTemplate(updatedData.getTemplate());
        }

        if (updatedData.getIsPremium() != null) {
            existing.setIsPremium(updatedData.getIsPremium());
        }

        //////////////////////////////////////////////////
        // PROFILE
        //////////////////////////////////////////////////

        if (updatedData.getProfileInfo() != null) {
            existing.setProfileInfo(
                    updatedData.getProfileInfo()
            );
        }

        //////////////////////////////////////////////////
        // CONTACT
        //////////////////////////////////////////////////

        if (updatedData.getContactInfo() != null) {
            existing.setContactInfo(
                    updatedData.getContactInfo()
            );
        }

        //////////////////////////////////////////////////
        // WORK EXPERIENCE
        //////////////////////////////////////////////////

        if (updatedData.getWorkExperiences() != null) {
            existing.setWorkExperiences(
                    updatedData.getWorkExperiences()
            );
        }

        //////////////////////////////////////////////////
        // EDUCATION
        //////////////////////////////////////////////////

        if (updatedData.getEducation() != null) {
            existing.setEducation(
                    updatedData.getEducation()
            );
        }

        //////////////////////////////////////////////////
        // SKILLS
        //////////////////////////////////////////////////

        if (updatedData.getSkills() != null) {
            existing.setSkills(
                    updatedData.getSkills()
            );
        }

        //////////////////////////////////////////////////
        // PROJECTS
        //////////////////////////////////////////////////

        if (updatedData.getProjects() != null) {
            existing.setProjects(
                    updatedData.getProjects()
            );
        }

        //////////////////////////////////////////////////
        // CERTIFICATIONS
        //////////////////////////////////////////////////

        if (updatedData.getCertifications() != null) {
            existing.setCertifications(
                    updatedData.getCertifications()
            );
        }

        //////////////////////////////////////////////////
        // LANGUAGES
        //////////////////////////////////////////////////

        if (updatedData.getLanguages() != null) {
            existing.setLanguages(
                    updatedData.getLanguages()
            );
        }

        //////////////////////////////////////////////////
        // INTERESTS
        //////////////////////////////////////////////////

        if (updatedData.getInterests() != null) {
            existing.setInterests(
                    updatedData.getInterests()
            );
        }

        //////////////////////////////////////////////////
// CUSTOM SECTIONS
//////////////////////////////////////////////////

if (updatedData.getCustomSections() != null) {
    existing.setCustomSections(
            updatedData.getCustomSections()
    );
}

        //////////////////////////////////////////////////
        // SAVE
        //////////////////////////////////////////////////

        Resume savedResume =
                resumeRepository.save(existing);

        return savedResume;
    }

    //////////////////////////////////////////////////
    // DELETE RESUME
    //////////////////////////////////////////////////

    public void deleteResume(
            String id,
            Object principal
    ) {

        AuthResponse response =
                authService.getProfile(principal);

        Resume existing =
                resumeRepository
                        .findByUserIdAndId(
                                response.getId(),
                                id
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        resumeRepository.delete(existing);
    }
}