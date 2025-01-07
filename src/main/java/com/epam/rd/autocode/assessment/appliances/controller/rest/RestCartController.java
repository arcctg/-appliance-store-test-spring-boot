package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Cart> viewCart() {
        return ResponseEntity.ok(cartService.getCurrentUserCart());
    }

    @Loggable
    @PostMapping("/add-item/{id}")
    public ResponseEntity<String> addItemToCart(@PathVariable Long id, @RequestParam Long number) {
        cartService.addItemToCart(id, number);
        return ResponseEntity.ok("Item added to cart");
    }

    @Loggable
    @PostMapping("/edit-item/{id}")
    public ResponseEntity<String> editItemInCart(@PathVariable Long id, @RequestParam Long number) {
        cartService.editItemInCart(id, number);
        return ResponseEntity.ok("Item edited in cart");
    }

    @Loggable
    @DeleteMapping("/delete-item/{id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable Long id) {
        cartService.deleteItemFromCart(id);
        return ResponseEntity.noContent().build();
    }
}
