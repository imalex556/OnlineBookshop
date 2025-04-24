package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.service.CartService;
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
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "customer-cart"; 
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam int quantity) {
        cartService.addToCart(id, quantity);
        return "redirect:/customer/books/details/" + id;
    }

    @PostMapping("/cart/update/{id}")
    public String updateCartItem(@PathVariable Long id, @RequestParam int quantity) {
        cartService.updateCartItem(id, quantity);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/customer/cart";
    }
}