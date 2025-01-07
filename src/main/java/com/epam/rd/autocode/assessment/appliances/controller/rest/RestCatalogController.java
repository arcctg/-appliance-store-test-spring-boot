package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog")
public class RestCatalogController {
    private final ApplianceService applianceService;

    public RestCatalogController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @Loggable
    @GetMapping
    public Page<Appliance> catalog(@PageableDefault(size = 9) Pageable pageable) {
        return applianceService.getAllAppliances(pageable);
    }
}
