package com.example.jobportal.services;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerApply;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerApplyRepository.findByJobSeekerProfile(jobSeekerProfile);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity jobPostActivity) {
        return jobSeekerApplyRepository.findByJobPostActivity(jobPostActivity);
    }
}
