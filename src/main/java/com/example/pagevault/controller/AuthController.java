package com.example.pagevault.controller;

import com.example.pagevault.model.User;
import com.example.pagevault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail()))) {
            model.addAttribute("error", "Username or email already exists");
            return "registration";
        }
        user.setRole(User.Role.USER);
        userService.saveUser(user);
        model.addAttribute("success", "Registration successful! You can now login.");
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}

