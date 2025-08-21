package com.example.jobportal.services;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.JobSeekerSave;
import com.example.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    private final JobSeekerSaveRepository jobSeekerSaveRepository;

    @Autowired
    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidateJob(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerSaveRepository.findByJobSeekerProfile(jobSeekerProfile);
    }

    public List<JobSeekerSave> getJobCandidates(JobPostActivity jobPostActivity) {
        return jobSeekerSaveRepository.findByJobPostActivity(jobPostActivity);
    }
}
