package com.epam.rd.autocode.assessment.appliances.exception;

public class ManufacturerNotFoundException extends NotFoundException {
    public ManufacturerNotFoundException(Long id) {
        super("Manufacturer with id %d not found".formatted(id));
    }
}
