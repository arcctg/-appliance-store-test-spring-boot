package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplianceService {
    void addAppliance(Appliance appliance);
    void updateAppliance(Appliance appliance);
    Appliance getApplianceById(Long id);
    void deleteApplianceById(Long id);
    Page<Appliance> getAllAppliances(Pageable pageable);
    List<Appliance> getAllAppliances();
    Category[] getCategories();
    List<Manufacturer> getManufacturers();
    PowerType[] getPowerTypes();
}
