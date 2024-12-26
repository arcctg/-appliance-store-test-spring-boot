package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private final ApplianceService applianceService;

    @Autowired
    public CatalogController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @Loggable
    @GetMapping
    public String catalog(Model model,
                          @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("appliances", applianceService.getAllAppliances(PageRequest.of(page, 9)));

        return "catalog/catalog";
    }
}
