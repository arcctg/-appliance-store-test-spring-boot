package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        Order orderToUpdate = getOrderById(order.getId());
        BeanUtils.copyProperties(order, orderToUpdate, "id");
        orderRepository.save(orderToUpdate);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not " +
                "found"));
    }

    @Override
    public void approveOrderById(Long id, boolean approved) {
        Order order = getOrderById(id);
        order.setApproved(approved);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).toList();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public int getOrdersSize() {
        return (int) orderRepository.count();
    }

    @Override
    public void addOrderRow(Long orderId, OrderRow orderRow) {
        Order order = getOrderById(orderId);
        order.getOrderRowSet().add(orderRow);
        orderRepository.save(order);
    }
}