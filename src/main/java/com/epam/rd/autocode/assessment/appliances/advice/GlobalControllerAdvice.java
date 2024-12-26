package com.epam.rd.autocode.assessment.appliances.advice;

import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final CartService cartService;
    private final ApplianceService appliancesService;

    @Autowired
    public GlobalControllerAdvice(CartService cartService, ApplianceService appliancesService) {
        this.cartService = cartService;
        this.appliancesService = appliancesService;
    }

    @ModelAttribute("cart")
    public Cart getCart() {
        return cartService.getCurrentUserCart();
    }
}
