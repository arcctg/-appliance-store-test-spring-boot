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
import org.springframework.web.bind.annotation.PatchMapping;
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
    public String getAllOrders(@PageableDefault(size = 5, sort = "id") Pageable pageable, Model model) {
        model.addAttribute("orders", orderService.getAllOrders(pageable));

        return "order/order";
    }

    @Loggable
    @PostMapping("/create")
    public String addOrder() {
        orderService.createOrder();

        return "redirect:/orders";
    }

    @Loggable
    @PatchMapping("/cancel")
    public String cancelOrder(@RequestParam("id") Long id, HttpServletRequest request) {
        orderService.updateStatusById(id, Status.CANCELLED);

        return redirectToReferer(request);
    }

    @Loggable
    @PatchMapping("/update-status")
    public String updateStatus(
            @RequestParam("id") Long id, @RequestParam("status") Status status, HttpServletRequest request) {
        orderService.updateStatusById(id, status);

        return redirectToReferer(request);
    }

    private String redirectToReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        return referer != null ? "redirect:" + referer : "redirect:/orders";
    }
}
