package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));
        Page<Order> orders = new PageImpl<>(Collections.emptyList());

        when(orderService.getAllOrders(pageable)).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/order"))
                .andExpect(model().attributeExists("pageable"));

        verify(orderService, times(1)).getAllOrders(pageable);
    }

    @Test
    void testAddOrder() throws Exception {
        mockMvc.perform(get("/orders/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService, times(1)).createOrder();
    }

    @Test
    void testCancelOrder() throws Exception {
        mockMvc.perform(get("/orders/cancel")
                        .param("id", "1")
                        .header("Referer", "/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService, times(1)).updateStatusById(1L, Status.CANCELLED);
    }

    @Test
    void testUpdateStatus() throws Exception {
        mockMvc.perform(post("/orders/update-status")
                        .param("id", "1")
                        .param("status", "DELIVERED")
                        .header("Referer", "/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService, times(1)).updateStatusById(1L, Status.DELIVERED);
    }
}
