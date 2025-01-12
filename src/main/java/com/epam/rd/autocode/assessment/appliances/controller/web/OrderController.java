package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Loggable
    @GetMapping
    public String getAllOrders(
            Model model,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        model.addAttribute("orders", orderService.getAllOrders(pageable));
        model.addAttribute("pageable", pageable);

        return "order/order";
    }

    @Loggable
    @GetMapping("/create")
    public String addOrder() {
        orderService.createOrder();

        return "redirect:/orders";
    }

    @Loggable
    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam("id") Long id, HttpServletRequest request) {
        orderService.updateStatusById(id, Status.CANCELLED);
        return "redirect:" + request.getHeader("Referer");
    }

    @Loggable
    @PostMapping("/update-status")
    public String updateStatus(HttpServletRequest request,
                               @RequestParam("id") Long id,
                               @RequestParam("status") Status status) {
        orderService.updateStatusById(id, status);

        return "redirect:" + request.getHeader("Referer");
    }

}
