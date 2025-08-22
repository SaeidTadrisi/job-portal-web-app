package com.example.jobportal.repository;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    List<JobSeekerSave> findByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);

    List<JobSeekerSave> findByJobPostActivity(JobPostActivity jobPostActivity);

}
