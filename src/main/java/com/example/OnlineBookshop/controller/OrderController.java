package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.Order;
import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("hasDiscount", user.isHasDiscount());
        model.addAttribute("orders", orderService.getUserOrders(session));
        return "customer-orders";
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, 
                                 HttpSession session, 
                                 Model model) {
        Order order = orderService.getOrderById(orderId, session);
        if (order == null) {
            return "redirect:/customer/orders";
        }
        model.addAttribute("order", order);
        return "customer-order-details";
    }
}