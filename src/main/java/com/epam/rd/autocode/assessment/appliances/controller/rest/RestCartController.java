package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class RestCartController {
    private final CartService cartService;

    public RestCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Loggable
    @GetMapping
    public Cart viewCart() {
        return cartService.getCurrentUserCart();
    }

    @Loggable
    @PostMapping("/add-item/{id}")
    public String addItemToCart(@PathVariable Long id, @RequestParam Long number) {
        cartService.addItemToCart(id, number);
        return "Item added to cart";
    }

    @Loggable
    @PostMapping("/edit-item/{id}")
    public String editItemInCart(@PathVariable Long id, @RequestParam Long number) {
        cartService.editItemInCart(id, number);
        return "Item edited in cart";
    }

    @Loggable
    @DeleteMapping("/delete-item/{id}")
    public void deleteItemFromCart(@PathVariable Long id) {
        cartService.deleteItemFromCart(id);
    }
}
