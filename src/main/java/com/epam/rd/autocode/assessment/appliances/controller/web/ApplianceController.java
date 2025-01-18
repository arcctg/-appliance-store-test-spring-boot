package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appliances")
public class ApplianceController {
    private final ApplianceService applianceService;

    public ApplianceController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @Loggable
    @GetMapping
    public String getAllAppliances(Model model, @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        model.addAttribute("appliances", applianceService.getAllAppliances(pageable));
        model.addAttribute("pageable", pageable);

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
    @PostMapping({"/add-appliance", "/edit-appliance"})
    public String addAppliance(@ModelAttribute Appliance appliance) {
        applianceService.saveAppliance(appliance);
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
    @GetMapping("/delete")
    public String deleteAppliance(@RequestParam("id") Long id, HttpServletRequest request) {
        applianceService.deleteApplianceById(id);
        return "redirect:" + request.getHeader("Referer");
    }
}

