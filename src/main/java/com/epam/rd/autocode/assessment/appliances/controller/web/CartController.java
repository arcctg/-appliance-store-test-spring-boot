package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private static final String REDIRECT_CART = "redirect:/cart";

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
    public String addItemToCart(
            HttpServletRequest request, @RequestParam Long applianceId, @RequestParam Long number) {
        cartService.addItemToCart(applianceId, number);

        return getRedirectUrl(request);
    }

    @Loggable
    @PostMapping("/edit-item")
    public String editItemInCart(@RequestParam Long orderId, @RequestParam Long number) {
        cartService.editItemInCart(orderId, number);
        return REDIRECT_CART;
    }

    @Loggable
    @GetMapping("/delete-item")
    public String deleteItemFromCart(@RequestParam Long orderId) {
        cartService.deleteItemFromCart(orderId);
        return REDIRECT_CART;
    }

    private String getRedirectUrl(HttpServletRequest request) {
        return "redirect:" + request.getHeader("Referer");
    }
}
