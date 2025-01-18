package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.model.Order;
import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRowRepository orderRowRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetAllOrdersAsClient() {
        mockSecurityContext("test@client.com");

        Client client = new Client();
        client.setEmail("test@client.com");
        when(userService.findUserByEmail("test@client.com")).thenReturn(client);

        Order order = new Order();
        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(order));

        when(orderRepository.findAllByClient(eq(client), any(PageRequest.class)))
                .thenReturn(ordersPage);

        Page<Order> result = orderService.getAllOrders(PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAllOrdersAsAdmin() {
        mockSecurityContext("admin@example.com");

        CustomUser user = mock(Employee.class);
        when(userService.findUserByEmail("admin@example.com")).thenReturn(user);

        Order order = new Order();
        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(ordersPage);

        Page<Order> result = orderService.getAllOrders(PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(orderRepository).findAll(any(PageRequest.class));
    }

    @Test
    void testCreateOrder() {
        mockSecurityContext("client@example.com");

        Cart cart = mock(Cart.class);
        Client client = mock(Client.class);
        when(cartService.getCurrentUserCart()).thenReturn(cart);
        when(cart.getClient()).thenReturn(client);
        when(cart.getTotal()).thenReturn(BigDecimal.valueOf(100.0));

        OrderRow orderRow = mock(OrderRow.class);
        List<OrderRow> orderRowList = Collections.singletonList(orderRow);
        when(cart.getOrderRowList()).thenReturn(orderRowList);

        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        Order order = orderService.createOrder();

        assertNotNull(order);
        verify(orderRowRepository, times(1)).save(orderRow);
        verify(cartService, times(1)).deleteCart(cart);
    }

    @Test
    void testUpdateStatusByIdWithEmployee() {
        mockSecurityContext("employee@example.com");

        Order order = mock(Order.class);
        Employee employee = mock(Employee.class);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userService.findUserByEmail("employee@example.com")).thenReturn(employee);

        orderService.updateStatusById(1L, Status.SHIPPED);

        verify(order).setStatus(Status.SHIPPED);
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdateStatusByIdWithoutEmployee() {
        mockSecurityContext("client@example.com");

        Order order = mock(Order.class);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        CustomUser user = mock(Client.class);
        when(userService.findUserByEmail("client@example.com")).thenReturn(user);

        orderService.updateStatusById(1L, Status.PENDING);

        verify(order).setStatus(Status.PENDING);
        verify(orderRepository).save(order);
        verify(order, times(0)).setEmployee(any(Employee.class));
    }

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.lenient().when(authentication.getName()).thenReturn(email);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
