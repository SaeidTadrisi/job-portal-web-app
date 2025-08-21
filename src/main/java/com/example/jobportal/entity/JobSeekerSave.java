package com.example.jobportal.entity;

import com.example.jobportal.repository.JobPostActivityRepository;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "job"})
})
public class JobSeekerSave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_account_id")
    private JobSeekerProfile jobSeekerProfile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "jobPostId")
    private JobPostActivity jobPostActivity;

    public JobSeekerSave() {
    }

    public JobSeekerSave(Integer id, JobSeekerProfile jobSeekerProfile, JobPostActivity jobPostActivity) {
        this.id = id;
        this.jobSeekerProfile = jobSeekerProfile;
        this.jobPostActivity = jobPostActivity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        this.jobSeekerProfile = jobSeekerProfile;
    }

    public JobPostActivity getJobPostActivity() {
        return jobPostActivity;
    }

    public void setJobPostActivity(JobPostActivity jobPostActivity) {
        this.jobPostActivity = jobPostActivity;
    }

    @Override
    public String toString() {
        return "JobSeekerSave{" +
                "id=" + id +
                ", jobSeekerProfile=" + jobSeekerProfile +
                ", jobPostActivity=" + jobPostActivity +
                '}';
    }
}
