package com.example.jobportal.services;


import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.RecruiterProfileRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UsersService usersService;


    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersService usersService) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersService = usersService;
    }

    public Optional<RecruiterProfile> getRecruiterProfile(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile save(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersService.getUserByEmail(authentication.getName());
            Optional<RecruiterProfile> recruiterProfile = getRecruiterProfile(user.getUserId());
            return recruiterProfile.orElse(null);
        }
        return null;
    }
}
