package com.epam.rd.autocode.assessment.appliances.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    private final IndexController indexController = new IndexController();

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

    @Test
    void testHome() throws Exception {
        // Test that the home page (index or "") returns the correct view
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())               // Ensure the status is 200 OK
                .andExpect(view().name("index"));     // Ensure the view name is "indexHome"
    }
}
