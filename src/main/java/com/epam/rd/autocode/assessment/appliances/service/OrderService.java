package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);
    void updateOrder(Order order);
    Order getOrderById(Long id);
    void approveOrderById(Long id, boolean approved);
    void deleteOrderById(Long id);
    List<Order> getAllOrders(Pageable pageable);
    List<Order> getAllOrders();
    int getOrdersSize();
    void addOrderRow(Long orderId , OrderRow orderRow);
}
