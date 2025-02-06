package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private static final String CART_COOKIE_NAME = "ANONYMOUS_CART_ID";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 днів
    private final CartRepository cartRepository;
    private final ApplianceService applianceService;
    private final OrderRowRepository orderRowRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    public CartServiceImpl(
            CartRepository cartRepository,
            ApplianceService applianceService,
            OrderRowRepository orderRowRepository,
            HttpServletRequest request,
            HttpServletResponse response,
            ClientRepository clientRepository,
            OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.applianceService = applianceService;
        this.orderRowRepository = orderRowRepository;
        this.request = request;
        this.response = response;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Cart getCurrentUserCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return cartRepository.findById(getAnonymousCartId()).orElseGet(() -> {
                Cart newCart = cartRepository.save(new Cart());
                setAnonymousCartId(newCart.getId());
                return newCart;
            });
        }

        return clientRepository
                .findByEmail(auth.getName())
                .map(client -> {
                    Cart clientCart = cartRepository.findByClient(client).orElseGet(() -> {
                        Cart cart = new Cart();
                        cart.setClient(client);
                        return cartRepository.save(cart);
                    });

                    cartRepository.findById(getAnonymousCartId()).ifPresent(anonymousCart -> {
                        mergeCarts(clientCart, anonymousCart);
                        cartRepository.delete(anonymousCart);
                        clearAnonymousCartId();
                    });

                    return clientCart;
                })
                .orElse(null);
    }

    public void mergeCarts(Cart userCart, Cart anonymousCart) {
        List<OrderRow> userOrderRowSet = userCart.getOrderRowList();
        for (OrderRow orderRow : anonymousCart.getOrderRowList()) {
            userOrderRowSet.stream()
                    .filter(o -> o.getAppliance()
                            .getId()
                            .equals(orderRow.getAppliance().getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingOrderRow -> {
                                existingOrderRow.setNumber(existingOrderRow.getNumber() + orderRow.getNumber());
                                existingOrderRow.setAmount(
                                        existingOrderRow.getAmount().add(orderRow.getAmount()));
                                orderRowRepository.delete(orderRow);
                            },
                            () -> {
                                orderRow.setCart(userCart);
                                userOrderRowSet.add(orderRow);
                            });
        }
    }

    @Override
    public void addItemToCart(Long applianceId, Long number) {
        Cart cart = getCurrentUserCart();
        Appliance appliance = applianceService.getApplianceById(applianceId);

        cart.getOrderRowList().stream()
                .filter(orderRow -> orderRow.getAppliance().getId().equals(applianceId))
                .findFirst()
                .ifPresentOrElse(
                        orderRow -> {
                            orderRow.setNumber(orderRow.getNumber() + number);
                            orderRow.setAmount(
                                    BigDecimal.valueOf(orderRow.getNumber()).multiply(appliance.getPrice()));
                        },
                        () -> {
                            OrderRow orderRow = new OrderRow();
                            orderRow.setCart(cart);
                            orderRow.setAppliance(appliance);
                            orderRow.setNumber(number);
                            orderRow.setAmount(BigDecimal.valueOf(number).multiply(appliance.getPrice()));
                            cart.getOrderRowList().add(orderRowRepository.save(orderRow));
                        });

        cartRepository.save(cart);
    }

    @Override
    public void editItemInCart(Long orderId, Long number) {
        Cart cart = getCurrentUserCart();

        cart.getOrderRowList().stream()
                .filter(orderRow -> orderRow.getId().equals(orderId))
                .findFirst()
                .ifPresent(orderRow -> {
                    orderRow.setNumber(number);
                    orderRow.setAmount(BigDecimal.valueOf(number)
                            .multiply(orderRow.getAppliance().getPrice()));
                    cartRepository.save(cart);
                });
    }

    @Override
    public void deleteItemFromCart(Long orderId) {
        orderRowRepository.deleteById(orderId);
    }

    @Override
    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public void deleteCartByClient(Client client) {
        cartRepository.findByClient(client).ifPresent(c -> {
            c.setClient(null);
            orderRowRepository.deleteAll(c.getOrderRowList());
            cartRepository.delete(c);
        });

        orderRepository.findAllByClient(client, Pageable.unpaged()).forEach(order -> {
            order.setClient(null);
            order.setStatus(Status.CANCELLED);
        });
    }

    private Long getAnonymousCartId() {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (CART_COOKIE_NAME.equals(cookie.getName())
                        && cookie.getValue().matches("\\d+")) {
                    return Long.valueOf(cookie.getValue());
                }
            }
        }
        return -1L;
    }

    private void setAnonymousCartId(Long cartId) {
        Cookie cookie = new Cookie(CART_COOKIE_NAME, cartId.toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    private void clearAnonymousCartId() {
        Cookie cookie = new Cookie(CART_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
