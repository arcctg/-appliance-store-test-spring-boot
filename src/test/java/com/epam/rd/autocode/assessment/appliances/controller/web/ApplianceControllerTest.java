package com.epam.rd.autocode.assessment.appliances.controller.web;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ApplianceControllerTest {

    private static final String BASE_URL = "/appliances";
    private static final Long APPLIANCE_ID = 1L;

    private MockMvc mockMvc;

    @Mock
    private ApplianceService applianceService;

    @InjectMocks
    private ApplianceController applianceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applianceController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void testGetAllAppliances() throws Exception {
        Appliance appliance = createTestAppliance();
        Page<Appliance> page = new PageImpl<>(List.of(appliance));

        when(applianceService.getAllAppliances(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(BASE_URL).param("page", "0").param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("appliance/appliances"))
            .andExpect(model().attributeExists("appliances"))
            .andExpect(model().attribute("appliances", page));

        verify(applianceService, times(1)).getAllAppliances(any(Pageable.class));
        verifyNoMoreInteractions(applianceService);
    }

    @Test
    void testAddApplianceForm() throws Exception {
        when(applianceService.getCategories()).thenReturn(Category.values());
        when(applianceService.getManufacturers()).thenReturn(List.of(new Manufacturer()));
        when(applianceService.getPowerTypes()).thenReturn(PowerType.values());

        mockMvc.perform(get(BASE_URL + "/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("appliance/newAppliance"))
            .andExpect(
                model().attributeExists("appliance", "categories", "manufacturers", "powerTypes"));

        verify(applianceService).getCategories();
        verify(applianceService).getManufacturers();
        verify(applianceService).getPowerTypes();
        verifyNoMoreInteractions(applianceService);
    }

    @Test
    void testAddAppliance() throws Exception {
        Appliance appliance = createTestAppliance();
        when(applianceService.saveAppliance(appliance)).thenReturn(appliance);

        mockMvc.perform(post(BASE_URL + "/add-appliance").flashAttr("appliance", appliance))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(BASE_URL));

        verify(applianceService).saveAppliance(appliance);
        verifyNoMoreInteractions(applianceService);
    }

    @Test
    void testEditApplianceForm() throws Exception {
        Appliance appliance = createTestAppliance();

        when(applianceService.getApplianceById(APPLIANCE_ID)).thenReturn(appliance);
        when(applianceService.getCategories()).thenReturn(Category.values());
        when(applianceService.getManufacturers()).thenReturn(List.of(new Manufacturer()));
        when(applianceService.getPowerTypes()).thenReturn(PowerType.values());

        mockMvc.perform(get(BASE_URL + "/edit").param("id", APPLIANCE_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("appliance/editAppliance"))
            .andExpect(
                model().attributeExists("appliance", "categories", "manufacturers", "powerTypes"));

        verify(applianceService, times(1)).getApplianceById(APPLIANCE_ID);
        verifyNoMoreInteractions(applianceService);
    }

    @Test
    void testDeleteAppliance() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/delete")
                .param("id", APPLIANCE_ID.toString())
                .header("Referer", BASE_URL))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(BASE_URL));

        verify(applianceService, times(1)).deleteApplianceById(APPLIANCE_ID);
        verifyNoMoreInteractions(applianceService);
    }

    private Appliance createTestAppliance() {
        Appliance appliance = new Appliance();
        appliance.setId(APPLIANCE_ID);
        appliance.setName("Test Appliance");
        appliance.setCategory(Category.BIG);
        appliance.setManufacturer(new Manufacturer());
        appliance.setPowerType(PowerType.AC110);
        appliance.setDescription("Test Description");
        appliance.setPrice(BigDecimal.valueOf(1));
        appliance.setCharacteristic("Test Characteristic");
        appliance.setModel("Test Model");
        appliance.setPower(500);

        return appliance;
    }
}
