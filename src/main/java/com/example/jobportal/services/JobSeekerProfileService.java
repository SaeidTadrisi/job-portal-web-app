package com.example.jobportal.services;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersService usersService;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersService usersService) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersService = usersService;
    }

    public Optional<JobSeekerProfile> getJobSeekerProfile(Integer id){
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile save(JobSeekerProfile jobSeekerProfile){
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentJobSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            Users user = usersService.getUserByEmail(userName);
            Optional<JobSeekerProfile> jobSeekerProfile = getJobSeekerProfile(user.getUserId());
            return jobSeekerProfile.orElse(null);
        }
        return null;
    }
}
