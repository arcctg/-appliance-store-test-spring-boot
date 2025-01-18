package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> getAllOrders(Pageable pageable);

    Order  createOrder();

    void updateStatusById(Long id, Status status);
}
