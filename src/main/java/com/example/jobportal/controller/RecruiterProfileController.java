package com.example.jobportal.controller;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.services.FileStorageService;
import com.example.jobportal.services.RecruiterProfileService;
import com.example.jobportal.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UsersService usersService;
    private final FileStorageService fileStorageService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, UsersService usersService, FileStorageService fileStorageService) {
        this.recruiterProfileService = recruiterProfileService;
        this.usersService = usersService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users user = usersService.getUserByEmail(currentUserName);
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getRecruiterProfile(user.getUserId());

            if (recruiterProfile.isPresent()){
                model.addAttribute("profile", recruiterProfile);
            }
        }
        return "recruiter_profile";
    }

    @PostMapping("/save")
    public String saveRecruiterProfile (RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile,
                         Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ((authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/login";
        }
        String currentUserName = authentication.getName();
        Users currentUser = usersService.getUserByEmail(currentUserName);
        recruiterProfile.setUser(currentUser);
        recruiterProfile.setUserAccountId(currentUser.getUserId());

        if (multipartFile !=null && !multipartFile.isEmpty()){
            try {
                String fileName = fileStorageService.saveFile("photos/recruiter/" + currentUser.getUserId(),
                        multipartFile);
                recruiterProfile.setProfilePhoto(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        recruiterProfileService.save(recruiterProfile);
        model.addAttribute("profile", recruiterProfile);

        return "redirect:/dashboard/";
    }
}
