package com.example.JobManagement.candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    CandidateEntity findByUsernameOrEmail(String username, String email);

    CandidateEntity findByUsername(String username);
}
