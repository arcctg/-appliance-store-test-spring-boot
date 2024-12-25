package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;

    @Autowired
    public OrderController(OrderService orderService, ClientService clientService,
                           EmployeeService employeeService, ApplianceService applianceService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.applianceService = applianceService;
    }

    @Before("")
    public void before() {
        orderService.addOrder(new Order());
    }

    @Loggable
    @GetMapping
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("appliances", applianceService.getAllAppliances());
        return "order/orders";
    }

    @Loggable
    @GetMapping("/add")
    public String addOrderForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("appliances", applianceService.getAllAppliances());
        return "order/newOrder";
    }

    @Loggable
    @PostMapping("/add")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.addOrder(order);
        return "redirect:/orders";
    }

    @Loggable
    @PostMapping("/add-row")
    public String addOrderRow(@RequestParam Long applianceId,
                              @RequestParam Long number) {
        Appliance appliance = applianceService.getApplianceById(applianceId);

        OrderRow orderRow = new OrderRow();
        orderRow.setAppliance(appliance);
        orderRow.setNumber(number);
        orderRow.setAmount(BigDecimal.valueOf(number).multiply(appliance.getPrice()));

        List<Order> orders = orderService.getAllOrders();
        Order order;

        if (orders.isEmpty()) {
            order = new Order();
            order.setOrderRowSet(new HashSet<>());
        } else {
            order = orders.get(0);
        }

        order.getOrderRowSet().add(orderRow);

        orderService.addOrder(order);

        return "redirect:/orders";
    }

    @Loggable
    @GetMapping("/edit")
    public String editOrderForm(@RequestParam Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("appliances", applianceService.getAllAppliances());
        return "order/editOrder";
    }

    @Loggable
    @PostMapping("/edit")
    public String updateOrder(@ModelAttribute Order order) {
        orderService.updateOrder(order);
        return "redirect:/orders";
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders";
    }

    @Loggable
    @GetMapping("/approve")
    public String approveOrder(@RequestParam("id") Long id) {
        orderService.approveOrderById(id, true);
        return "redirect:/orders";
    }

    @Loggable
    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam("id") Long id) {
        orderService.approveOrderById(id, false);
        return "redirect:/orders";
    }
}
