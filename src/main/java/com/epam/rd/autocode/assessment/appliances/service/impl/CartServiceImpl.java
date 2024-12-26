package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceInOrderRepository;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ApplianceService applianceService;

    public CartServiceImpl(CartRepository cartRepository, ApplianceService applianceService) {
        this.cartRepository = cartRepository;
        this.applianceService = applianceService;
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

    @Override
    public void addItemToCart(Long applianceId, Long number) {
        Cart cart = getCurrentUserCart();
        OrderRow orderRow = new OrderRow();
        Appliance appliance = applianceService.getApplianceById(applianceId);

        orderRow.setAppliance(appliance);
        orderRow.setNumber(number);
        orderRow.setAmount(BigDecimal.valueOf(number).multiply(appliance.getPrice()));
        cart.getOrderRowSet().add(orderRow);

        cartRepository.save(cart);
    }

    @Override
    public void editItemInCart(Long orderId, Long number) {
        Cart cart = getCurrentUserCart();
        Optional<OrderRow> orderRowOptional = cart.getOrderRowSet().stream()
                .filter(orderRow -> orderRow.getId().equals(orderId))
                .findFirst();

        if (orderRowOptional.isPresent()) {
            OrderRow orderRow = orderRowOptional.get();
            orderRow.setNumber(number);
            orderRow.setAmount(BigDecimal.valueOf(number).multiply(orderRow.getAppliance().getPrice()));
            cartRepository.save(cart);
        }
    }

    @Override
    public void deleteItemFromCart(Long orderId) {
        Cart cart = getCurrentUserCart();
        cart.getOrderRowSet().removeIf(orderRow -> orderRow.getId().equals(orderId));
        cartRepository.save(cart);
    }
}
