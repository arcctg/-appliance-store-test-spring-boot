package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Loggable
    @GetMapping
    public String getAllManufacturers(
            Model model,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers(pageable));
        model.addAttribute("pageable", pageable);

        return "manufacture/manufacturers";
    }

    @Loggable
    @GetMapping("/add")
    public String addManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacture/newManufacturer";
    }

    @Loggable
    @GetMapping("/edit")
    public String editManufacturerForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("manufacturer", manufacturerService.getManufacturerById(id));
        return "manufacture/editManufacturer";
    }

    @Loggable
    @PostMapping({"/add-manufacturer", "/edit-manufacturer"})
    public String addManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.saveManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteManufacturer(@RequestParam("id") Long id, HttpServletRequest request) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:" + request.getHeader("Referer");
    }
}