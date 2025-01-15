package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplianceServiceImplTest {

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ApplianceServiceImpl applianceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAppliance() {
        Appliance appliance = new Appliance();
        when(applianceRepository.save(appliance)).thenReturn(appliance);

        Appliance savedAppliance = applianceService.saveAppliance(appliance);

        assertEquals(appliance, savedAppliance);
        verify(applianceRepository, times(1)).save(appliance);
    }

    @Test
    void testGetApplianceById_Found() {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        when(applianceRepository.findById(1L)).thenReturn(Optional.of(appliance));

        Appliance foundAppliance = applianceService.getApplianceById(1L);

        assertEquals(appliance, foundAppliance);
        verify(applianceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetApplianceById_NotFound() {
        when(applianceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApplianceNotFoundException.class, () -> applianceService.getApplianceById(1L));
        verify(applianceRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteApplianceById() {
        Long id = 1L;

        applianceService.deleteApplianceById(id);

        verify(applianceRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllAppliances() {
        Pageable pageable = mock(Pageable.class);
        List<Appliance> appliances = Arrays.asList(new Appliance(), new Appliance());
        Page<Appliance> appliancePage = new PageImpl<>(appliances);
        when(applianceRepository.findAll(pageable)).thenReturn(appliancePage);

        Page<Appliance> result = applianceService.getAllAppliances(pageable);

        assertEquals(appliancePage, result);
        verify(applianceRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetCategories() {
        Category[] categories = Category.values();

        assertArrayEquals(categories, applianceService.getCategories());
    }

    @Test
    void testGetManufacturers() {
        List<Manufacturer> manufacturers = Arrays.asList(new Manufacturer(), new Manufacturer());
        when(manufacturerRepository.findAll()).thenReturn(manufacturers);

        List<Manufacturer> result = applianceService.getManufacturers();

        assertEquals(manufacturers, result);
        verify(manufacturerRepository, times(1)).findAll();
    }

    @Test
    void testGetPowerTypes() {
        PowerType[] powerTypes = PowerType.values();

        assertArrayEquals(powerTypes, applianceService.getPowerTypes());
    }
}
