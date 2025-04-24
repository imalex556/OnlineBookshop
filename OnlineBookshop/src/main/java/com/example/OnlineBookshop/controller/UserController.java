package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "signup";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            return "signup";
        }
        
        userRepository.save(user);
        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, 
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {
        User user = userRepository.findByUsername(username)
            .orElse(null);
        
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        
        session.setAttribute("user", user);
        
        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/books";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}