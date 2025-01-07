package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manufacturers")
public class RestManufacturerController {
    private final ManufacturerService manufacturerService;

    public RestManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Loggable
    @GetMapping
    public Page<Manufacturer> getAllManufacturers(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return manufacturerService.getAllManufacturers(pageable);
    }

    @Loggable
    @GetMapping("/{id}")
    public Manufacturer getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getManufacturerById(id);
    }

    @Loggable
    @PostMapping
    public Manufacturer addManufacturer(@RequestBody Manufacturer manufacturer) {
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @Loggable
    @PutMapping("/{id}")
    public Manufacturer editManufacturer(@PathVariable Long id, @RequestBody Manufacturer manufacturer) {
        manufacturer.setId(id);
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @Loggable
    @DeleteMapping("/{id}")
    public void deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturerById(id);
    }
}
