package com.example.jobportal.controller;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.services.RecruiterProfileService;
import com.example.jobportal.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UsersService usersService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, UsersService usersService) {
        this.recruiterProfileService = recruiterProfileService;
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users user = usersService.getUserByEmail(currentUserName).orElseThrow(() ->
                    new UsernameNotFoundException("User not found!"));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getRecruiterProfile(user.getUserId());

            if (recruiterProfile.isPresent()){
                model.addAttribute("profile", recruiterProfile);
            }
        }
        return "recruiter_profile";
    }

    @GetMapping("/addNew")
    
}
