package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final OrderRowRepository orderRowRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, UserService userService, OrderRowRepository orderRowRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderRowRepository = orderRowRepository;
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        CustomUser user = userService.findUserByEmail(getAuth().getName());

        if (user instanceof Client client) {
            return orderRepository.findAllByClient(client, pageable);
        }

        return orderRepository.findAll(pageable);
    }

    @Override
    public Order createOrder() {
        Cart cart = cartService.getCurrentUserCart();
        if (cart == null) return null;

        Order order = new Order();
        List<OrderRow> orderRowList = cart.getOrderRowList();

        order.setClient(cart.getClient());
        order.setTotal(cart.getTotal());
        orderRepository.save(order);

        orderRowList.forEach(orderRow -> {
            orderRow.setOrder(order);
            orderRow.setCart(null);
            orderRowRepository.save(orderRow);
        });
        cartService.deleteCart(cart);

        order.setOrderRowList(new ArrayList<>(orderRowList));
        return orderRepository.save(order);
    }

    @Override
    public void updateStatusById(Long id, Status status) {
        orderRepository.findById(id).ifPresent(order -> {
            CustomUser user = userService.findUserByEmail(getAuth().getName());

            if (user instanceof Employee employee) {
                order.setEmployee(employee);
            }

            order.setStatus(status);
            orderRepository.save(order);
        });
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
