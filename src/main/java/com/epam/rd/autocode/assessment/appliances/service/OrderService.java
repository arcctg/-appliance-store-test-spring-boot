package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void addOrder(Orders order);
    void updateOrder(Orders order);
    Orders getOrderById(Long id);
    void approveOrderById(Long id, boolean approved);
    void deleteOrderById(Long id);
    List<Orders> getAllOrders(Pageable pageable);
    List<Orders> getAllOrders();
    int getOrdersSize();
    void addOrderRow(Long orderId , OrderRow orderRow);
}
