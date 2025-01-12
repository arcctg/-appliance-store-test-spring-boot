package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Cart;

public interface CartService {
    Cart getCurrentUserCart();

    void addItemToCart(Long applianceId, Long number);

    void editItemInCart(Long orderId, Long number);

    void deleteItemFromCart(Long orderId);

    void deleteCart(Cart cart);
}
