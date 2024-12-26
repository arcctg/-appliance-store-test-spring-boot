package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceServiceImpl implements ApplianceService {
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ApplianceServiceImpl(ApplianceRepository applianceRepository,
                                ManufacturerRepository manufacturerRepository) {
        this.applianceRepository = applianceRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void addAppliance(Appliance appliance) {
        applianceRepository.save(appliance);
    }

    @Override
    public void updateAppliance(Appliance appliance) {
        Appliance applianceToUpdate = getApplianceById(appliance.getId());
        BeanUtils.copyProperties(appliance, applianceToUpdate, "id");
        applianceRepository.save(applianceToUpdate);
    }

    @Override
    public Appliance getApplianceById(Long id) {
        return applianceRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Appliance not found"));
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
    public List<Appliance> getAllAppliances() {
        return applianceRepository.findAll();
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
