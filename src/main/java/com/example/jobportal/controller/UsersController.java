package com.example.jobportal.controller;

import com.example.jobportal.entity.Users;
import com.example.jobportal.entity.UsersType;
import com.example.jobportal.repository.UsersRepository;
import com.example.jobportal.services.UserTypesService;
import com.example.jobportal.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private final UserTypesService userTypesService;
    private final UsersService usersService;
    @Autowired
    public UsersController(UserTypesService userTypesService, UsersService usersService) {
        this.userTypesService = userTypesService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> allUsersTypes = userTypesService.getAll();
        model.addAttribute("getAllTypes", allUsersTypes);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping ("/register/new")
    public String userRegistration(@Valid Users users, Model model){
        Optional<Users> userByEmail = usersService.getUserByEmail(users.getEmail());
        if (userByEmail.isPresent()){
            model.addAttribute("error", "Email already registered, try to login or register with other email.");
            List<UsersType> allUsersTypes = userTypesService.getAll();
            model.addAttribute("getAllTypes", allUsersTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
        usersService.addNew(users);
        return "dashboard";
    }

}
