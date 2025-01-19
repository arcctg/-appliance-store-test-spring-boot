package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ApplianceServiceImpl implements ApplianceService {
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;

    public ApplianceServiceImpl(ApplianceRepository applianceRepository,
                                ManufacturerRepository manufacturerRepository) {
        this.applianceRepository = applianceRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Appliance saveAppliance(Appliance appliance) {
        return applianceRepository.save(appliance);
    }

    @Override
    public Appliance getApplianceById(Long id) {
        return applianceRepository.findById(id).orElseThrow(() -> new ApplianceNotFoundException(id));
    }

    @Override
    public void deleteApplianceById(Long id) {
        applianceRepository.deleteById(id);
    }

    @Override
    public Page<Appliance> getAllAppliances(Pageable pageable) {
        return applianceRepository.findAll(pageable);
    }

    @Override
    public Category[] getCategories() {
        return Category.values();
    }

    @Override
    public List<Manufacturer> getManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public PowerType[] getPowerTypes() {
        return PowerType.values();
    }
}
