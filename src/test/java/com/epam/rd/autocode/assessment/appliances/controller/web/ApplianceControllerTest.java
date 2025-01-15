package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

class ApplianceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplianceService applianceService;

    @InjectMocks
    private ApplianceController applianceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applianceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllAppliances() throws Exception {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Test Appliance");

        Page<Appliance> page = new PageImpl<>(List.of(appliance));

        when(applianceService.getAllAppliances(any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/appliances")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/appliances"))
                .andExpect(model().attributeExists("appliances"))
                .andExpect(model().attribute("appliances", page))
                .andExpect(model().attributeExists("pageable"));

        verify(applianceService).getAllAppliances(any(PageRequest.class));
    }


    @Test
    void testAddApplianceForm() throws Exception {
        when(applianceService.getCategories()).thenReturn(Category.values());
        when(applianceService.getManufacturers()).thenReturn(List.of(new Manufacturer()));
        when(applianceService.getPowerTypes()).thenReturn(PowerType.values());

        mockMvc.perform(get("/appliances/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/newAppliance"))
                .andExpect(model().attributeExists("appliance", "categories", "manufacturers", "powerTypes"));
    }


    @Test
    void testAddAppliance() throws Exception {
        mockMvc.perform(post("/appliances/add-appliance")
                        .param("name", "Test Appliance")
                        .param("category", "BIG"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        verify(applianceService).saveAppliance(any(Appliance.class));
    }

    @Test
    void testEditApplianceForm() throws Exception {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Test Appliance");

        when(applianceService.getApplianceById(1L)).thenReturn(appliance);
        when(applianceService.getCategories()).thenReturn(Category.values());
        when(applianceService.getManufacturers()).thenReturn(List.of(new Manufacturer()));
        when(applianceService.getPowerTypes()).thenReturn(PowerType.values());

        mockMvc.perform(get("/appliances/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/editAppliance"))
                .andExpect(model().attributeExists("appliance", "categories", "manufacturers", "powerTypes"));

        verify(applianceService).getApplianceById(1L);
    }

    @Test
    void testDeleteAppliance() throws Exception {
        mockMvc.perform(get("/appliances/delete").param("id", "1").header("Referer", "/appliances"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        verify(applianceService).deleteApplianceById(1L);
    }
}
