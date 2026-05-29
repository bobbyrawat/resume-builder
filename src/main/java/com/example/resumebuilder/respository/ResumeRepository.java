package com.example.resumebuilder.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.resumebuilder.document.Resume;

public interface ResumeRepository extends MongoRepository<Resume, String> {

  List<Resume>  findByUserIdOrderByUpdatedAtDesc(String userId);

 Optional<Resume> findByUserIdAndId(String userID, String id);
    
}
