package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceInOrderRepository;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCurrentUserCart() {
        return cartRepository.findById(1L).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setOrderRowSet(new HashSet<>());
            cartRepository.save(cart);
            return cart;
        });
    }
}
