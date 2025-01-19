package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    private static final String REDIRECT_CART = "redirect:/cart";
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Loggable
    @GetMapping
    public String viewCart() {
        return "cart/cart";
    }

    @Loggable
    @PostMapping("/add-item")
    public String addItemToCart(@RequestParam Long applianceId, @RequestParam Long number, HttpServletRequest request) {
        cartService.addItemToCart(applianceId, number);

        return redirectToReferer(request);
    }

    @Loggable
    @PatchMapping("/edit-item")
    public String editItemInCart(@RequestParam Long orderId, @RequestParam Long number) {
        cartService.editItemInCart(orderId, number);

        return REDIRECT_CART;
    }

    @Loggable
    @DeleteMapping("/delete-item")
    public String deleteItemFromCart(@RequestParam Long orderId) {
        cartService.deleteItemFromCart(orderId);

        return REDIRECT_CART;
    }

    private String redirectToReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        return referer != null ? "redirect:" + referer : REDIRECT_CART;
    }
}
