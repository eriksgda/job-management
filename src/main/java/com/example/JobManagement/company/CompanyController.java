package com.example.JobManagement.company;

import com.example.JobManagement.exceptions.UserOrEmailAlreadyExistException;
import com.example.JobManagement.jobs.CreateJobDTO;
import com.example.JobManagement.jobs.JobEntity;
import com.example.JobManagement.jobs.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @Autowired
    private JobService jobService;

    @PostMapping("/")
    public ResponseEntity<Object> createCompany(@Valid @RequestBody CompanyEntity company){
        try {
            CompanyEntity result = this.service.createCompany(company);
            return ResponseEntity.ok().body(result);
        } catch (UserOrEmailAlreadyExistException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/job")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Object> createJob(@Valid @RequestBody CreateJobDTO jobDTO, HttpServletRequest request){
        try {
            Object companyId = request.getAttribute("company_id");
            JobEntity job = JobEntity.builder()
                    .companyId(UUID.fromString(companyId.toString()))
                    .benefits(jobDTO.getBenefits())
                    .description(jobDTO.getDescription())
                    .level(jobDTO.getLevel())
                    .build();

            this.jobService.createJob(job);

            return ResponseEntity.ok().body(job);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("error");
        }
    }
}
