package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Cart;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.CartRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ApplianceService applianceService;

    @Mock
    private ClientService clientService;

    @Mock
    private OrderRowRepository orderRowRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddItemToCart_NewItem() {
        // Mock authentication setup
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        // Prepare mock client and cart
        Client client = new Client();
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setOrderRowList(new ArrayList<>());

        // Mock behavior for client and cart retrieval
        when(clientService.getClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Prepare mock appliance
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setPrice(BigDecimal.valueOf(100));

        when(applianceService.getApplianceById(1L)).thenReturn(appliance);

        // Mock saving the order row
        when(orderRowRepository.save(any(OrderRow.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call method under test
        cartService.addItemToCart(1L, 2L);

        // Assert and verify cart contents
        assertEquals(1, cart.getOrderRowList().size());
        OrderRow addedOrderRow = cart.getOrderRowList().get(0);
        assertNotNull(addedOrderRow);  // Ensure it's not null
        assertEquals(appliance, addedOrderRow.getAppliance());
        assertEquals(2L, addedOrderRow.getNumber());
        assertEquals(BigDecimal.valueOf(200), addedOrderRow.getAmount());

        // Verify cart was saved
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testGetCurrentUserCart_AnonymousUser() {
        Authentication authentication = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Cart anonymousCart = new Cart();
        when(cartRepository.findByClientIsNull()).thenReturn(Optional.of(anonymousCart));
        when(cartRepository.save(anonymousCart)).thenReturn(anonymousCart);

        Cart result = cartService.getCurrentUserCart();

        assertEquals(anonymousCart, result);
        verify(cartRepository, times(1)).findByClientIsNull();
        verify(cartRepository, times(1)).save(anonymousCart);
    }

    @Test
    void testEditItemInCart_ItemExists() {
        // Mock authentication setup
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        // Prepare mock client and cart
        Client client = new Client();
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setOrderRowList(new ArrayList<>());

        // Initialize the Appliance and its BigDecimal field (e.g., price or some other field)
        Appliance appliance = new Appliance();
        appliance.setPrice(new BigDecimal("10.0")); // Mock a non-null BigDecimal value

        OrderRow orderRow = new OrderRow();
        orderRow.setId(1L);
        orderRow.setAppliance(appliance); // Set the appliance with price
        orderRow.setNumber(1L);
        cart.getOrderRowList().add(orderRow);

        // Mock client and cart retrieval
        when(clientService.getClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Call method under test
        cartService.editItemInCart(1L, 3L);

        // Assert updated cart contents
        assertEquals(3L, cart.getOrderRowList().get(0).getNumber());
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    void testDeleteItemFromCart() {
        cartService.deleteItemFromCart(1L);

        verify(orderRowRepository, times(1)).deleteById(1L);
    }

    @Test
    void testMergeCarts() {
        Cart userCart = new Cart();
        userCart.setOrderRowList(new ArrayList<>());

        Cart anonymousCart = new Cart();
        OrderRow anonymousOrderRow = new OrderRow();
        anonymousOrderRow.setAppliance(new Appliance());
        anonymousOrderRow.getAppliance().setId(1L);
        anonymousOrderRow.setNumber(2L);
        anonymousOrderRow.setAmount(BigDecimal.valueOf(200));
        anonymousCart.setOrderRowList(List.of(anonymousOrderRow));

        cartService.mergeCarts(userCart, anonymousCart);

        assertEquals(1, userCart.getOrderRowList().size());
        assertEquals(2L, userCart.getOrderRowList().get(0).getNumber());
        assertEquals(BigDecimal.valueOf(200), userCart.getOrderRowList().get(0).getAmount());
    }
}
