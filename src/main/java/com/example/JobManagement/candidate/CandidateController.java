package com.example.JobManagement.candidate;

import com.example.JobManagement.exceptions.JobNotFoundException;
import com.example.JobManagement.exceptions.UserNotFoundException;
import com.example.JobManagement.exceptions.UserOrEmailAlreadyExistException;
import com.example.JobManagement.jobs.Applyjobs.ApplyJobEntity;
import com.example.JobManagement.jobs.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidate", description = "Candidate Infos")
public class CandidateController {

    @Autowired
    private CandidateService service;

    @PostMapping("/")
    @Operation(summary = "Create a candidate profile", description = "create candidate :)")
    public ResponseEntity<Object> createCandidate(@Valid @RequestBody CandidateEntity candidate) {
        try {
            CandidateEntity result = this.service.createUser(candidate);
            return ResponseEntity.ok().body(result);
        } catch (UserOrEmailAlreadyExistException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("error");
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get all info of the candidate", description = "List candidate info :)")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getProfileCandidate(HttpServletRequest request) {
        Object id = request.getAttribute("candidate_id");

        try {
            CandidateProfileResponseDTO profile = this.service.getProfileUser(UUID.fromString(id.toString()));
            return ResponseEntity.ok().body(profile);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("error");
        }
    }

    @GetMapping("/view_jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "List all jobs with filter to candidate", description = "List jobs :)")
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> getAllJobsByFilter(@RequestParam String filter) {
        try {
            return this.service.getAllJobsByFilter(filter);
            //return ResponseEntity.ok().body(jobs);
        } catch (Exception exception) {
            return null;
        }
    }

    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Apply to a job", description = "Apply jobs :)")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID jobId) {
        Object id = request.getAttribute("candidate_id");
        try {
            ApplyJobEntity result = this.service.applyJob(UUID.fromString(id.toString()), jobId);
            return ResponseEntity.ok().body(result);
        } catch (UserNotFoundException | JobNotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("error");
        }
    }


}
