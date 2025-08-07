package com.example.jobportal.services;

import com.example.jobportal.entity.UsersType;
import com.example.jobportal.repository.UserTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypesService {

    private final UserTypesRepository userTypesRepository;

    @Autowired
    public UserTypesService(UserTypesRepository userTypesRepository) {
        this.userTypesRepository = userTypesRepository;
    }

    public List<UsersType> getAll(){
        return userTypesRepository.findAll();
    }
}
