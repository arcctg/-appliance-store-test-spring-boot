package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Loggable
    @GetMapping
    public String getAllManufacturers(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sort) {
        size = size == null ? manufacturerService.getManufacturersSize() : size;
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers(PageRequest.of(page,
                size, Sort.by(sort))));

        return "manufacture/manufacturers";
    }

    @Loggable
    @GetMapping("/add")
    public String addManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacture/newManufacturer";
    }

    @Loggable
    @PostMapping("/add-manufacturer")
    public String addManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }

    @Loggable
    @GetMapping("/edit")
    public String editManufacturerForm(@RequestParam("id") Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        model.addAttribute("manufacturer", manufacturer);
        return "manufacture/editManufacturer";
    }

    @Loggable
    @PostMapping("/edit-manufacturer")
    public String editManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteManufacturer(@RequestParam("id") Long id) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:/manufacturers";
    }
}