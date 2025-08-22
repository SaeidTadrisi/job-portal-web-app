package com.example.jobportal.controller;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.JobSeekerSave;
import com.example.jobportal.entity.Users;
import com.example.jobportal.services.JobPostActivityService;
import com.example.jobportal.services.JobSeekerProfileService;
import com.example.jobportal.services.JobSeekerSaveService;
import com.example.jobportal.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final UsersService usersService;

    public JobSeekerSaveController(JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService, UsersService usersService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.usersService = usersService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            JobSeekerSave jobSeekerSave = new JobSeekerSave();
            String userName = authentication.getName();
            Users user = usersService.getUserByEmail(userName);
            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService.getJobSeekerProfile(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (jobSeekerProfile.isPresent() && jobPostActivity != null) {

                jobSeekerSave.setJobSeekerProfile(jobSeekerProfile.get());
                jobSeekerSave.setJobPostActivity(jobPostActivity);
            } else {
                throw new RuntimeException("User not found.");
            }
            jobSeekerSaveService.save(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {

        List<JobPostActivity> jobPostActivities = new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUserProfile();
        List<JobSeekerSave> candidateJob = jobSeekerSaveService.getCandidateJob((JobSeekerProfile) currentUserProfile);
        for (JobSeekerSave jobSeekerSave : candidateJob) {
            jobPostActivities.add(jobSeekerSave.getJobPostActivity());
        }
        model.addAttribute("jobPost", jobPostActivities);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }

}
