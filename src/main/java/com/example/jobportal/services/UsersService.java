package com.example.jobportal.services;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.RecruiterProfileRepository;
import com.example.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Users addNew(Users users){
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        Users savedUser = usersRepository.save(users);
        int userTypeId = users.getUserType().getUserTypeId();
        if (userTypeId == 1){
            recruiterProfileRepository.save(new RecruiterProfile(users));
        }else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(users));
        }
        return savedUser;

    }

    public Optional<Users> getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }


}
