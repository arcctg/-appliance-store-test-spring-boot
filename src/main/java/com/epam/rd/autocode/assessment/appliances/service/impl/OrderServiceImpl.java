package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.orderRepository = ordersRepository;
    }

    @Override
    public void addOrder(Orders order) {
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(Orders order) {
        Orders orderToUpdate = getOrderById(order.getId());
        BeanUtils.copyProperties(order, orderToUpdate, "id");
        orderRepository.save(orderToUpdate);
    }

    @Override
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not " +
                "found"));
    }

    @Override
    public void approveOrderById(Long id, boolean approved) {
        Orders order = getOrderById(id);
        order.setApproved(approved);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Orders> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).toList();
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public int getOrdersSize() {
        return (int) orderRepository.count();
    }

    @Override
    public void addOrderRow(Long orderId, OrderRow orderRow) {
        Orders order = getOrderById(orderId);
        order.getOrderRowSet().add(orderRow);
        orderRepository.save(order);
    }
}