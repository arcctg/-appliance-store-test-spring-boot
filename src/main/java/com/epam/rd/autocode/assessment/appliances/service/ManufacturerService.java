package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManufacturerService {
    void addManufacturer(Manufacturer manufacturer);
    void updateManufacturer(Manufacturer manufacturer);
    Manufacturer getManufacturerById(Long id);
    void deleteManufacturerById(Long id);
    Page<Manufacturer> getAllManufacturers(Pageable pageable);
}
