package com.example.OnlineBookshop.service;

import com.example.OnlineBookshop.model.*;
import com.example.OnlineBookshop.repository.BookRepository;
import com.example.OnlineBookshop.repository.OrderRepository;
import com.example.OnlineBookshop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        BookRepository bookRepository,
                        CartService cartService,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        if (user.getShippingAddress() == null || user.getShippingAddress().isEmpty()) {
            throw new RuntimeException("Shipping address is required");
        }
        if (user.getPaymentMethod() == null || user.getPaymentMethod().isEmpty()) {
            throw new RuntimeException("Payment method is required");
        }
        if ("Credit Card".equals(user.getPaymentMethod()) &&
            (user.getCardNumber() == null || user.getCardNumber().isEmpty())) {
            throw new RuntimeException("Card details are required for credit card payment");
        }

        double totalAmount = cart.getTotalPrice();
        if (user.isHasDiscount()) {
            totalAmount *= 0.8;
            user.setHasDiscount(false);
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(user.getShippingAddress());
        order.setPaymentMethod(user.getPaymentMethod());
        order.setCardNumber(user.getCardNumber());
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.APPROVED);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            order.addItem(orderItem);

            Book book = cartItem.getBook();
            book.setStockQuantity(book.getStockQuantity() - cartItem.getQuantity());
            bookRepository.save(book);
        }

        int newOrderCount = user.getOrderCount() + 1;
        user.setOrderCount(newOrderCount);
        if (newOrderCount >= 10) {
            user.setHasDiscount(true);
            user.setOrderCount(0);
        }
        userRepository.save(user); 

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(session);
        return savedOrder;
    }

    public List<Order> getUserOrders(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderById(Long orderId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        return orderRepository.findById(orderId)
                .filter(order -> order.getUser().getUserId().equals(user.getUserId()))
                .orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
