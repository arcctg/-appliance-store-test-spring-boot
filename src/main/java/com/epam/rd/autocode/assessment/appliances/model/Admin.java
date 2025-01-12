package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Admin extends CustomUser {
    public Admin(Long id, String name, String email, String password, Role role) {
        super(id, name, email, password, role);
    }
}
