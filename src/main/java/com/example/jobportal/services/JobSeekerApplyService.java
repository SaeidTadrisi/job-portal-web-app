package com.example.jobportal.services;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerApply;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile userAccountId) {
        return jobSeekerApplyRepository.findByJobSeekerProfile(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity jobId) {
        return jobSeekerApplyRepository.findByJobPostActivity(jobId);
    }

    public void save(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
