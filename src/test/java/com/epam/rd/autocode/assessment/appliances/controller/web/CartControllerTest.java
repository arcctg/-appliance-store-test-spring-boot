package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testViewCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart"));

        verifyNoInteractions(cartService);
    }

    @Test
    void testAddItemToCart() throws Exception {
        mockMvc.perform(post("/cart/add-item")
                        .param("applianceId", "1")
                        .param("number", "2")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).addItemToCart(1L, 2L);
    }

    @Test
    void testEditItemInCart() throws Exception {
        mockMvc.perform(post("/cart/edit-item")
                        .param("orderId", "1")
                        .param("number", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).editItemInCart(1L, 3L);
    }

    @Test
    void testDeleteItemFromCart() throws Exception {
        mockMvc.perform(get("/cart/delete-item")
                        .param("orderId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).deleteItemFromCart(1L);
    }
}
