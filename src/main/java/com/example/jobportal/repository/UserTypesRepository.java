package com.example.jobportal.repository;

import com.example.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserTypesRepository extends JpaRepository<UsersType, Integer> {
}
