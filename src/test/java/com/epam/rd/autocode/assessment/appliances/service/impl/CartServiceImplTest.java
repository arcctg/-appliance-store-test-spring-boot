package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ApplianceService applianceService;

    @Mock
    private ClientService clientService;

    @Mock
    private OrderRowRepository orderRowRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CartServiceImpl cartService;

    @Captor
    private ArgumentCaptor<OrderRow> orderRowCaptor;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddItemToCart_NewItem() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        Client client = new Client();
        Cart cart = createCart(client);

        when(clientService.getClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Appliance appliance = createAppliance(BigDecimal.valueOf(100));
        when(applianceService.getApplianceById(1L)).thenReturn(appliance);

        cartService.addItemToCart(1L, 2L);

        verify(orderRowRepository).save(orderRowCaptor.capture());
        OrderRow savedOrderRow = orderRowCaptor.getValue();
        assertAll(
            () -> assertEquals(appliance, savedOrderRow.getAppliance()),
            () -> assertEquals(2L, savedOrderRow.getNumber()),
            () -> assertEquals(BigDecimal.valueOf(200), savedOrderRow.getAmount())
        );
        verify(cartRepository).save(cart);
    }

    @Test
    void testGetCurrentUserCart_AnonymousUser() {
        Authentication anonymousAuthentication = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(anonymousAuthentication);

        Cart anonymousCart = new Cart();
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(anonymousCart));

        Cart result = cartService.getCurrentUserCart();

        assertSame(anonymousCart, result);
        verify(cartRepository).findById(anyLong());
        verify(cartRepository, never()).save(anonymousCart);
    }

    @Test
    void testEditItemInCart_ItemExists() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        Client client = new Client();
        Cart cart = createCart(client);

        Appliance appliance = createAppliance(BigDecimal.TEN);
        OrderRow orderRow = createOrderRow(appliance, 1L);
        cart.getOrderRowList().add(orderRow);

        when(clientService.getClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));

        cartService.editItemInCart(1L, 3L);

        assertEquals(3L, orderRow.getNumber());
        assertEquals(BigDecimal.valueOf(30), orderRow.getAmount());
        verify(cartRepository).save(cart);
    }

    @Test
    void testDeleteItemFromCart() {
        cartService.deleteItemFromCart(1L);
        verify(orderRowRepository).deleteById(1L);
    }

    @Test
    void testMergeCarts() {
        Cart userCart = createCart(new Client());
        Cart anonymousCart = new Cart();
        anonymousCart.setOrderRowList(List.of(createOrderRow(createAppliance(BigDecimal.valueOf(100)), 2L)));

        cartService.mergeCarts(userCart, anonymousCart);

        assertEquals(1, userCart.getOrderRowList().size());
        OrderRow mergedOrderRow = userCart.getOrderRowList().get(0);
        assertEquals(2L, mergedOrderRow.getNumber());
        assertEquals(BigDecimal.valueOf(200), mergedOrderRow.getAmount());
    }

    private Cart createCart(Client client) {
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setOrderRowList(new ArrayList<>());
        return cart;
    }

    private Appliance createAppliance(BigDecimal price) {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setPrice(price);
        return appliance;
    }

    private OrderRow createOrderRow(Appliance appliance, Long number) {
        OrderRow orderRow = new OrderRow();
        orderRow.setId(1L);
        orderRow.setAppliance(appliance);
        orderRow.setNumber(number);
        orderRow.setAmount(BigDecimal.valueOf(number).multiply(appliance.getPrice()));
        return orderRow;
    }
}
