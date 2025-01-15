package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ManufacturerNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManufacturerServiceImplTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    private Manufacturer manufacturer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock manufacturer and Pageable setup
        manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Manufacturer Name");
        pageable = mock(Pageable.class);
    }

    @Test
    void testSaveManufacturer() {
        // Mock repository behavior
        when(manufacturerRepository.save(manufacturer)).thenReturn(manufacturer);

        // Call service method
        Manufacturer savedManufacturer = manufacturerService.saveManufacturer(manufacturer);

        // Verify results
        assertNotNull(savedManufacturer);
        assertEquals(manufacturer.getId(), savedManufacturer.getId());
        assertEquals(manufacturer.getName(), savedManufacturer.getName());
        verify(manufacturerRepository, times(1)).save(manufacturer);
    }

    @Test
    void testGetManufacturerById_Found() {
        // Mock repository behavior
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));

        // Call service method
        Manufacturer foundManufacturer = manufacturerService.getManufacturerById(1L);

        // Verify results
        assertNotNull(foundManufacturer);
        assertEquals(manufacturer.getId(), foundManufacturer.getId());
        assertEquals(manufacturer.getName(), foundManufacturer.getName());
        verify(manufacturerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetManufacturerById_NotFound() {
        // Mock repository behavior
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.getManufacturerById(1L));
        verify(manufacturerRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteManufacturerById() {
        // Call service method
        manufacturerService.deleteManufacturerById(1L);

        // Verify that repository's deleteById was called
        verify(manufacturerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllManufacturers() {
        // Create a Page object with mock manufacturers
        Page<Manufacturer> manufacturerPage = new PageImpl<>(List.of(manufacturer));

        // Mock repository behavior
        when(manufacturerRepository.findAll(pageable)).thenReturn(manufacturerPage);

        // Call service method
        Page<Manufacturer> result = manufacturerService.getAllManufacturers(pageable);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(manufacturerRepository, times(1)).findAll(pageable);
    }
}
