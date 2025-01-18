package com.epam.rd.autocode.assessment.appliances.exception;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(Long id) {
        super("Employee with id %d not found".formatted(id));
    }
}
