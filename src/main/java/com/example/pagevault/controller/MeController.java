package com.example.pagevault.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeController {

    @GetMapping("/me")
    public String me(Authentication authentication, Model model) {
        model.addAttribute("username", authentication != null ? authentication.getName() : null);
        return "me";
    }
}

