package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerProfileController {

    private final UserRepository userRepository;

    @Autowired
    public CustomerProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user = userRepository.findById(user.getUserId()).orElse(null);
        model.addAttribute("user", user);
        return "customer-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser, 
                              HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        User user = userRepository.findById(currentUser.getUserId()).orElse(null);
        if (user != null) {
            user.setEmail(updatedUser.getEmail());
            user.setShippingAddress(updatedUser.getShippingAddress());
            user.setPaymentMethod(updatedUser.getPaymentMethod());
            user.setCardNumber(updatedUser.getCardNumber());
            user.setExpiryDate(updatedUser.getExpiryDate());
            user.setCvv(updatedUser.getCvv());
            userRepository.save(user);
            session.setAttribute("user", user);
        }
        return "redirect:/customer/profile?success";
    }
}