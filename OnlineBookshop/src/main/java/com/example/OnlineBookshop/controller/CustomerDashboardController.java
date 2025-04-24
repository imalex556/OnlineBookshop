package com.example.OnlineBookshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

    @GetMapping("/dashboard")
    public String customerDashboard(Model model) {
        return "customer-dashboard";
    }
}