package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appliances")
public class ApplianceController {
    private final ApplianceService applianceService;

    @Autowired
    public ApplianceController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @Loggable
    @GetMapping
    public String getAllAppliances(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String order) {
        model.addAttribute("appliances", applianceService.getAllAppliances(PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(order), sort))));
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        return "appliance/appliances";
    }

    @Loggable
    @GetMapping("/add")
    public String addApplianceForm(Model model) {
        model.addAttribute("appliance", new Appliance());
        model.addAttribute("categories", applianceService.getCategories());
        model.addAttribute("manufacturers", applianceService.getManufacturers());
        model.addAttribute("powerTypes", applianceService.getPowerTypes());

        return "appliance/newAppliance";
    }

    @Loggable
    @PostMapping("/add-appliance")
    public String addAppliance(@ModelAttribute Appliance appliance) {
        applianceService.addAppliance(appliance);
        return "redirect:/appliances";
    }

    @Loggable
    @GetMapping("/edit")
    public String editApplianceForm(@RequestParam("id") Long id, Model model) {
        Appliance appliance = applianceService.getApplianceById(id);
        model.addAttribute("appliance", appliance);
        model.addAttribute("categories", applianceService.getCategories());
        model.addAttribute("manufacturers", applianceService.getManufacturers());
        model.addAttribute("powerTypes", applianceService.getPowerTypes());
        return "appliance/editAppliance";
    }

    @Loggable
    @PostMapping("/edit-appliance")
    public String editAppliance(@ModelAttribute Appliance appliance) {
        applianceService.updateAppliance(appliance);
        return "redirect:/appliances";
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteAppliance(@RequestParam("id") Long id) {
        applianceService.deleteApplianceById(id);
        return "redirect:/appliances";
    }
}

