package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.Order;
import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.service.CartService;
import com.example.OnlineBookshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CheckoutController {
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        boolean hasRequiredDetails = user.getShippingAddress() != null && !user.getShippingAddress().isEmpty() &&
                                   user.getPaymentMethod() != null && !user.getPaymentMethod().isEmpty() &&
                                   (!"Credit Card".equals(user.getPaymentMethod()) || 
                                    (user.getCardNumber() != null && !user.getCardNumber().isEmpty()));

        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("user", user);
        model.addAttribute("hasRequiredDetails", hasRequiredDetails);
        return "customer-checkout";
    }

    @PostMapping("/checkout/place-order")
    public String placeOrder(HttpSession session, Model model) {
        try {
            Order order = orderService.createOrder(session);
            return "redirect:/customer/orders/" + order.getOrderId() + "?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return showCheckout(session, model);
        }
    }
}