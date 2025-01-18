package com.epam.rd.autocode.assessment.appliances.controller.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class ManufacturerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private Model model;

    @InjectMocks
    private ManufacturerController manufacturerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(manufacturerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllManufacturers() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));
        List<Manufacturer> manufacturers = Collections.singletonList(new Manufacturer(1L, "Test Manufacturer"));
        Page<Manufacturer> page = new PageImpl<>(manufacturers);

        when(manufacturerService.getAllManufacturers(pageable)).thenReturn(page);

        mockMvc.perform(get("/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacture/manufacturers"))
                .andExpect(model().attributeExists("pageable"));

        verify(manufacturerService, times(1)).getAllManufacturers(pageable);
    }

    @Test
    void testAddManufacturerForm() throws Exception {
        mockMvc.perform(get("/manufacturers/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacture/newManufacturer"))
                .andExpect(model().attributeExists("manufacturer"));
    }

    @Test
    void testEditManufacturerForm() throws Exception {
        Manufacturer manufacturer = new Manufacturer(1L, "Test Manufacturer");

        when(manufacturerService.getManufacturerById(1L)).thenReturn(manufacturer);

        mockMvc.perform(get("/manufacturers/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacture/editManufacturer"))
                .andExpect(model().attributeExists("manufacturer"));

        verify(manufacturerService, times(1)).getManufacturerById(1L);
    }

    @Test
    void testAddManufacturer() throws Exception {
        Manufacturer manufacturer = new Manufacturer(1L, "New Manufacturer");

        mockMvc.perform(post("/manufacturers/add-manufacturer")
                        .flashAttr("manufacturer", manufacturer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));

        verify(manufacturerService, times(1)).saveManufacturer(manufacturer);
    }

    @Test
    void testDeleteManufacturer() throws Exception {
        mockMvc.perform(get("/manufacturers/delete").param("id", "1").header("Referer", "/manufacturers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));

        verify(manufacturerService, times(1)).deleteManufacturerById(1L);
    }
}
