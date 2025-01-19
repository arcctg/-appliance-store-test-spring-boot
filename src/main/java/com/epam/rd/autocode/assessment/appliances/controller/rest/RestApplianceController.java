package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appliances")
public class RestApplianceController {
    private final ApplianceService applianceService;

    public RestApplianceController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @Loggable
    @GetMapping
    public ResponseEntity<Page<Appliance>> getAllAppliances(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(applianceService.getAllAppliances(pageable));
    }

    @Loggable
    @GetMapping("/{id}")
    public ResponseEntity<Appliance> getApplianceById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(applianceService.getApplianceById(id));
    }

    @Loggable
    @PostMapping
    public ResponseEntity<Appliance> addAppliance(@RequestBody @Valid Appliance appliance) {
        return ResponseEntity.ok(applianceService.saveAppliance(appliance));
    }

    @Loggable
    @PutMapping("/{id}")
    public ResponseEntity<Appliance> editAppliance(@PathVariable @Min(1) Long id, @RequestBody @Valid Appliance appliance) {
        appliance.setId(id);
        return ResponseEntity.ok(applianceService.saveAppliance(appliance));
    }

    @Loggable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliance(@PathVariable @Min(1) Long id) {
        applianceService.deleteApplianceById(id);
        return ResponseEntity.noContent().build();
    }
}
