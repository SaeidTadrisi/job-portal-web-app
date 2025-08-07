package com.example.jobportal.services;

import com.example.jobportal.entity.*;
import com.example.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiterId){
        List<RecruiterJobProjection> recruiterJobs = jobPostActivityRepository.getRecruiterJobs(recruiterId);

        List<RecruiterJobsDto> recruiterJobsDtoList = new ArrayList<>();

        for (RecruiterJobProjection rec : recruiterJobs) {
            JobLocation jobLocation = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            JobCompany jobCompany = new JobCompany(rec.getCompanyId(), rec.getCompanyName(), "");
            recruiterJobsDtoList.add(new RecruiterJobsDto(rec.getTotalCandidates(), rec.getJob_post_id(),
                    rec.getJob_title(), jobLocation, jobCompany));
        }
        return recruiterJobsDtoList;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(()-> new RuntimeException("Job not found!"));
    }
}
