package com.epam.rd.autocode.assessment.appliances.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplTest {

    private static final Long APPLIANCE_ID = 1L;
    private static final String APPLIANCE_NAME = "Test Appliance";

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ApplianceServiceImpl applianceService;

    @Test
    void testSaveAppliance() {
        Appliance appliance = createTestAppliance();
        when(applianceRepository.save(appliance)).thenReturn(appliance);

        Appliance savedAppliance = applianceService.saveAppliance(appliance);

        assertEquals(appliance, savedAppliance);
        verify(applianceRepository, times(1)).save(appliance);
    }

    @Test
    void testGetApplianceById_Found() {
        Appliance appliance = createTestAppliance();
        when(applianceRepository.findById(APPLIANCE_ID)).thenReturn(Optional.of(appliance));

        Appliance foundAppliance = applianceService.getApplianceById(APPLIANCE_ID);

        assertEquals(appliance, foundAppliance);
        verify(applianceRepository, times(1)).findById(APPLIANCE_ID);
    }

    @Test
    void testGetApplianceById_NotFound() {
        when(applianceRepository.findById(APPLIANCE_ID)).thenReturn(Optional.empty());

        assertThrows(ApplianceNotFoundException.class, () -> applianceService.getApplianceById(APPLIANCE_ID));
        verify(applianceRepository, times(1)).findById(APPLIANCE_ID);
    }

    @Test
    void testDeleteApplianceById() {
        applianceService.deleteApplianceById(APPLIANCE_ID);

        verify(applianceRepository, times(1)).deleteById(APPLIANCE_ID);
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

    private Appliance createTestAppliance() {
        Appliance appliance = new Appliance();
        appliance.setId(APPLIANCE_ID);
        appliance.setName(APPLIANCE_NAME);
        return appliance;
    }
}
