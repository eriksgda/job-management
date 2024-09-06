package com.example.JobManagement.candidate;

import com.example.JobManagement.exceptions.UserOrEmailAlreadyExistException;
import com.example.JobManagement.jobs.JobEntity;
import com.example.JobManagement.jobs.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository  repository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity createUser(CandidateEntity candidate){
        if (this.repository.findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail()) != null){
            throw new UserOrEmailAlreadyExistException();
        }
        String encodedPassword = this.passwordEncoder.encode(candidate.getPassword());
        candidate.setPassword(encodedPassword);

        return this.repository.save(candidate);
    }

    public CandidateProfileResponseDTO getProfileUser(UUID candidateId){
        Optional<CandidateEntity> candidate = this.repository.findById(candidateId);

        if (candidate.isEmpty()){
            throw new UsernameNotFoundException("User not found.");
        }

        return CandidateProfileResponseDTO.builder()
                .id(candidate.get().getId())
                .name(candidate.get().getName())
                .username(candidate.get().getUsername())
                .email(candidate.get().getEmail())
                .description(candidate.get().getDescription())
                .build();
    }

    public List<JobEntity> getAllJobsByFilter(String filter){
        return this.jobRepository.findByDescriptionContaining(filter);
    }
}
