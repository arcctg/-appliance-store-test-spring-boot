package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Employee extends CustomUser {
    @NotEmpty(message = "Please provide your department")
    @Size(max = 100, message = "Department cannot be longer than 100 characters")
    private String department;

    public Employee(Long id, String name, String email, String password, Role role,
                    String department) {
        super(id, name, email, password, role);
        this.department = department;
    }
}
