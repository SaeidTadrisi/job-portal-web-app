package com.example.jobportal.services;


import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;


    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Optional<RecruiterProfile> getRecruiterProfile(Integer id){
        return recruiterProfileRepository.findById(id);
    }
}
