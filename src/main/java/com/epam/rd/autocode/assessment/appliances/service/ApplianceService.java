package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplianceService {
    Appliance saveAppliance(Appliance appliance);

    Appliance getApplianceById(Long id);

    void deleteApplianceById(Long id);

    Page<Appliance> getAllAppliances(Pageable pageable);

    Category[] getCategories();

    List<Manufacturer> getManufacturers();

    PowerType[] getPowerTypes();
}
