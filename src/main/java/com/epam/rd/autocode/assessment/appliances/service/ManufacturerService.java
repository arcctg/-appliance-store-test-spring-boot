package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufacturerService {
    void addManufacturer(Manufacturer manufacturer);
    void updateManufacturer(Manufacturer manufacturer);
    Manufacturer getManufacturerById(Long id);
    void deleteManufacturerById(Long id);
    List<Manufacturer> getAllManufacturers(Pageable pageable);
    int getManufacturersSize();
}
