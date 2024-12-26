package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping()
    public String viewCart(Model model) {
        // Assuming you have a method to get the current user's cart
        Cart cart = cartService.getCurrentUserCart();
        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    @ModelAttribute("cart")
    public Cart getCart() {
        return cartService.getCurrentUserCart();
    }
}
