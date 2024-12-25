package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceRepository applianceRepository;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository,
                                   ApplianceRepository applianceRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.applianceRepository = applianceRepository;
    }

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }

    @Override
    public void updateManufacturer(Manufacturer manufacturer) {
        Manufacturer manufacturerToUpdate = getManufacturerById(manufacturer.getId());
        BeanUtils.copyProperties(manufacturer, manufacturerToUpdate, "id");
        manufacturerRepository.save(manufacturerToUpdate);
    }

    @Override
    public Manufacturer getManufacturerById(Long id) {
        return manufacturerRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Manufacturer not found"));
    }

    @Override
    public void deleteManufacturerById(Long id) {
//        List<Appliance> appliances = applianceRepository.findByManufacturerId(id);
//
//        for (Appliance appliance : appliances) {
//            appliance.setManufacturer("Gr");
//            applianceRepository.save(appliance);
//        }

        manufacturerRepository.deleteById(id);
    }

    @Override
    public List<Manufacturer> getAllManufacturers(Pageable pageable) {
        return manufacturerRepository.findAll(pageable).toList();
    }

    @Override
    public int getManufacturersSize() {
        return manufacturerRepository.findAll().size();
    }
}
