package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.model.Order;
import com.example.OnlineBookshop.repository.UserRepository;
import com.example.OnlineBookshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final OrderService orderService;

    @Autowired
    public AdminDashboardController(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        userRepository.deleteById(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String listOrders(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin-orders";
    }

    @PostMapping("/orders/update-status/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, 
                                  @RequestParam String status,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        orderService.updateOrderStatus(orderId, Order.OrderStatus.valueOf(status));
        return "redirect:/admin/orders";
    }
}