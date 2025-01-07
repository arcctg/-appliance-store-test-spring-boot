package com.epam.rd.autocode.assessment.appliances.exception;

public class ApplianceNotFoundException extends RuntimeException {
    public ApplianceNotFoundException(Long id) {
        super("Appliance with id %d not found".formatted(id));
    }
}
