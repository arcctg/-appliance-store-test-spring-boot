package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ManufacturerNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceImplTest {

    private static final Long MANUFACTURER_ID = 1L;
    private static final String MANUFACTURER_NAME = "Test Manufacturer";

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    private Manufacturer manufacturer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        manufacturer = createTestManufacturer();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testSaveManufacturer() {
        when(manufacturerRepository.save(manufacturer)).thenReturn(manufacturer);

        Manufacturer savedManufacturer = manufacturerService.saveManufacturer(manufacturer);

        assertNotNull(savedManufacturer);
        assertEquals(MANUFACTURER_ID, savedManufacturer.getId());
        assertEquals(MANUFACTURER_NAME, savedManufacturer.getName());
        verify(manufacturerRepository, times(1)).save(manufacturer);
    }

    @Test
    void testGetManufacturerById_Found() {
        when(manufacturerRepository.findById(MANUFACTURER_ID)).thenReturn(Optional.of(manufacturer));

        Manufacturer foundManufacturer = manufacturerService.getManufacturerById(MANUFACTURER_ID);

        assertNotNull(foundManufacturer);
        assertEquals(MANUFACTURER_ID, foundManufacturer.getId());
        assertEquals(MANUFACTURER_NAME, foundManufacturer.getName());
        verify(manufacturerRepository, times(1)).findById(MANUFACTURER_ID);
    }

    @Test
    void testGetManufacturerById_NotFound() {
        when(manufacturerRepository.findById(MANUFACTURER_ID)).thenReturn(Optional.empty());

        assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.getManufacturerById(MANUFACTURER_ID));
        verify(manufacturerRepository, times(1)).findById(MANUFACTURER_ID);
    }

    @Test
    void testDeleteManufacturerById() {
        manufacturerService.deleteManufacturerById(MANUFACTURER_ID);

        verify(manufacturerRepository, times(1)).deleteById(MANUFACTURER_ID);
    }

    @Test
    void testGetAllManufacturers() {
        Page<Manufacturer> manufacturerPage = new PageImpl<>(List.of(manufacturer));

        when(manufacturerRepository.findAll(pageable)).thenReturn(manufacturerPage);

        Page<Manufacturer> result = manufacturerService.getAllManufacturers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(manufacturerRepository, times(1)).findAll(pageable);
    }

    private Manufacturer createTestManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(MANUFACTURER_ID);
        manufacturer.setName(MANUFACTURER_NAME);
        return manufacturer;
    }
}
