package com.example.pagevault.controller;

import com.example.pagevault.model.Purchase;
import com.example.pagevault.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/purchases")
    public String purchases(Authentication authentication, Model model) {
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        String username = authentication != null ? authentication.getName() : "";
        List<Purchase> purchases = isAdmin
                ? purchaseService.getAllPurchasesLatestFirst()
                : purchaseService.getPurchasesForUser(username);

        model.addAttribute("purchases", purchases);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        return "purchases";
    }
}

