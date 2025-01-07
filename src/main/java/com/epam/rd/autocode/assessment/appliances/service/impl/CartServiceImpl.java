package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ApplianceService applianceService;
    private final ClientService clientService;
    private final OrderRowRepository orderRowRepository;

    public CartServiceImpl(CartRepository cartRepository, ApplianceService applianceService,
                           ClientService clientService, OrderRowRepository orderRowRepository) {
        this.cartRepository = cartRepository;
        this.applianceService = applianceService;
        this.clientService = clientService;
        this.orderRowRepository = orderRowRepository;
    }

    @Override
    public Cart getCurrentUserCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return cartRepository.save(cartRepository.findByClientIsNull().orElse(new Cart()));
        }

        return clientService.getClientByEmail(auth.getName())
                .map(client -> {
                    Cart clientCart = cartRepository.findByClient(client)
                            .orElseGet(() -> {
                                Cart cart = new Cart();
                                cart.setClient(client);
                                return cartRepository.save(cart);
                            });

                    cartRepository.findByClientIsNull().ifPresent(anonymousCart -> {
                        mergeCarts(clientCart, anonymousCart);
                        cartRepository.delete(anonymousCart);
                    });

                    return clientCart;
                }).orElse(null);
    }

    private void mergeCarts(Cart userCart, Cart anonymousCart) {
        Set<OrderRow> userOrderRowSet = userCart.getOrderRowSet();
        for (OrderRow orderRow : anonymousCart.getOrderRowSet()) {
            userOrderRowSet.stream()
                    .filter(o -> o.getAppliance().getId().equals(orderRow.getAppliance().getId()))
                    .findFirst()
                    .ifPresentOrElse(existingOrderRow -> {
                        existingOrderRow.setNumber(existingOrderRow.getNumber() + orderRow.getNumber());
                        existingOrderRow.setAmount(existingOrderRow.getAmount().add(orderRow.getAmount()));
                    }, () -> userOrderRowSet.add(orderRow));
        }
    }

    @Override
    public void addItemToCart(Long applianceId, Long number) {
        Cart cart = getCurrentUserCart();
        Appliance appliance = applianceService.getApplianceById(applianceId);

        cart.getOrderRowSet().stream()
                .filter(orderRow -> orderRow.getAppliance().getId().equals(applianceId))
                .findFirst()
                .ifPresentOrElse(orderRow -> {
                    orderRow.setNumber(orderRow.getNumber() + number);
                    orderRow.setAmount(BigDecimal.valueOf(orderRow.getNumber()).multiply(appliance.getPrice()));
                }, () -> {
                    OrderRow orderRow = new OrderRow();
                    orderRow.setAppliance(appliance);
                    orderRow.setNumber(number);
                    orderRow.setAmount(BigDecimal.valueOf(number).multiply(appliance.getPrice()));
                    cart.getOrderRowSet().add(orderRowRepository.save(orderRow));
                });

        cartRepository.save(cart);
    }

    @Override
    public void editItemInCart(Long orderId, Long number) {
        getCurrentUserCart().getOrderRowSet().stream()
                .filter(orderRow -> orderRow.getId().equals(orderId))
                .findFirst()
                .ifPresent(orderRow -> {
                    orderRow.setNumber(number);
                    orderRow.setAmount(BigDecimal.valueOf(number).multiply(orderRow.getAppliance().getPrice()));
                    cartRepository.save(getCurrentUserCart());
                });
    }

    @Override
    public void deleteItemFromCart(Long orderId) {
        Cart cart = getCurrentUserCart();
        cart.getOrderRowSet().removeIf(orderRow -> orderRow.getId().equals(orderId));
        cartRepository.save(cart);
    }
}
