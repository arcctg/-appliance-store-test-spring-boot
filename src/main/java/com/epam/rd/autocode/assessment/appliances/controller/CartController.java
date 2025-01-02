package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Loggable
    @GetMapping()
    public String viewCart(Model model) {
        return "cart/cart";
    }

    @Loggable
    @PostMapping("/add-item")
    public String addItemToCart(@RequestParam Long applianceId,
                                @RequestParam Long number) {
        cartService.addItemToCart(applianceId, number);
        return "redirect:/catalog";
    }

    @Loggable
    @PostMapping("/edit-item")
    public String editItemInCart(@RequestParam Long orderId,
                                 @RequestParam Long number) {
        cartService.editItemInCart(orderId, number);
        return "redirect:/cart";
    }

    @Loggable
    @GetMapping("/delete-item")
    public String deleteItemFromCart(@RequestParam Long orderId) {
        cartService.deleteItemFromCart(orderId);
        return "redirect:/cart";
    }
}
