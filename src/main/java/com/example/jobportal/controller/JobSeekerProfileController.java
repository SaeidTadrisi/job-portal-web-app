package com.example.jobportal.controller;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.Skills;
import com.example.jobportal.entity.Users;
import com.example.jobportal.services.FileStorageService;
import com.example.jobportal.services.JobSeekerProfileService;
import com.example.jobportal.services.UsersService;
import com.example.jobportal.util.FileDownloadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;

    private final UsersService usersService;

    private final FileStorageService fileStorageService;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersService usersService, FileStorageService fileStorageService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersService = usersService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String JobSeekerProfile(Model model) {

        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        List<Skills> skills = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersService.getUserByEmail(authentication.getName());
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getJobSeekerProfile(user.getUserId());

            if (seekerProfile.isPresent()) {
                jobSeekerProfile = seekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("profile", jobSeekerProfile);
        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Unnecessary ->

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersService.getUserByEmail(authentication.getName());
            jobSeekerProfile.setUser(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        List<Skills> skillsList = new ArrayList<>();
        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills", skillsList);

        for (Skills skill : jobSeekerProfile.getSkills()) {
            skill.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";

        if (!Objects.equals(image.getOriginalFilename(), "")) {
            imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }
        if (!(Objects.equals(pdf.getOriginalFilename(), ""))) {
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        jobSeekerProfileService.save(jobSeekerProfile);

        try {
            fileStorageService.saveFile("photos/candidate/" + jobSeekerProfile.getUserAccountId(), image);
            fileStorageService.saveFile("photos/candidate/" + jobSeekerProfile.getUserAccountId(), pdf);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {

        JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.getJobSeekerProfile(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found!"));
        model.addAttribute("profile", jobSeekerProfile);
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,
                                            @RequestParam(value = "userID") String userId) {

        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource("photos/candidate/" + userId, fileName);
        } catch (IOException io) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
