package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session));
        model.addAttribute("hasDiscount", user.isHasDiscount());
        return "customer-cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam int quantity, HttpSession session) {
        cartService.addToCart(id, quantity, session);
        return "redirect:/customer/books/details/" + id;
    }

    @PostMapping("/cart/update/{id}")
    public String updateCartItem(@PathVariable Long id, @RequestParam int quantity, HttpSession session) {
        cartService.updateCartItem(id, quantity, session);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(id, session);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/customer/cart";
    }
    
    @ModelAttribute("cartItemCount")
    public int getCartItemCount(HttpSession session) {
        return cartService.getCartItemCount(session);
    }
}