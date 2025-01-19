package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> addItemToCart(@PathVariable @Min(1) Long id, @RequestParam @Min(1) Long number) {
        cartService.addItemToCart(id, number);
        return ResponseEntity.ok("Item added to cart");
    }

    @Loggable
    @PatchMapping("/edit-item/{id}")
    public ResponseEntity<String> editItemInCart(@PathVariable @Min(1) Long id, @RequestParam @Min(1) Long number) {
        cartService.editItemInCart(id, number);
        return ResponseEntity.ok("Item edited in cart");
    }

    @Loggable
    @DeleteMapping("/delete-item/{id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable @Min(1) Long id) {
        cartService.deleteItemFromCart(id);
        return ResponseEntity.noContent().build();
    }
}
