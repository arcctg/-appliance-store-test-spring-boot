package com.epam.rd.autocode.assessment.appliances.controller.web;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.rd.autocode.assessment.appliances.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private static final String BASE_URL = "/cart";
    private static final Long APPLIANCE_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private static final Long NUMBER_TWO = 2L;
    private static final Long NUMBER_THREE = 3L;

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testViewCart() throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(view().name("cart/cart"));

        verifyNoInteractions(cartService);
    }

    @Test
    void testAddItemToCart() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add-item")
                .param("applianceId", APPLIANCE_ID.toString())
                .param("number", NUMBER_TWO.toString())
                .header("Referer", BASE_URL))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(BASE_URL));

        verify(cartService).addItemToCart(APPLIANCE_ID, NUMBER_TWO);
        verifyNoMoreInteractions(cartService);
    }

    @Test
    void testEditItemInCart() throws Exception {
        mockMvc.perform(post(BASE_URL + "/edit-item")
                .param("orderId", ORDER_ID.toString())
                .param("number", NUMBER_THREE.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(BASE_URL));

        verify(cartService).editItemInCart(ORDER_ID, NUMBER_THREE);
        verifyNoMoreInteractions(cartService);
    }

    @Test
    void testDeleteItemFromCart() throws Exception {
        mockMvc.perform(get(BASE_URL + "/delete-item")
                .param("orderId", ORDER_ID.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(BASE_URL));

        verify(cartService).deleteItemFromCart(ORDER_ID);
        verifyNoMoreInteractions(cartService);
    }
}

