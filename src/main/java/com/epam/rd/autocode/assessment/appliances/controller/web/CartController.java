package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

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
    public String addItemToCart(HttpServletRequest request,
                                @RequestParam Long applianceId,
                                @RequestParam Long number) {
        cartService.addItemToCart(applianceId, number);
        return "redirect:" + request.getHeader("Referer");
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
