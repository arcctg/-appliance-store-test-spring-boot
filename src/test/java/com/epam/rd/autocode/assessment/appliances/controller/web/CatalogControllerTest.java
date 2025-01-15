package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CatalogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplianceService applianceService;

    @InjectMocks
    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(catalogController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testCatalog() throws Exception {
        // Arrange: Create a mock pageable result
        PageRequest pageable = PageRequest.of(0, 9);
        Page<Appliance> mockPage = new PageImpl<>(Collections.emptyList());
        when(applianceService.getAllAppliances(pageable)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/catalog"))
                .andExpect(model().attributeExists("appliances"));

        verify(applianceService, times(1)).getAllAppliances(any(Pageable.class));
    }
}
