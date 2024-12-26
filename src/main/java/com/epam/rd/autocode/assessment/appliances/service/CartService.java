package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Cart;

public interface CartService {
    Cart getCurrentUserCart();

    void addItemToCart(Long applianceId, Long number);
}
