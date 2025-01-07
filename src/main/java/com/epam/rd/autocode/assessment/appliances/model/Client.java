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
public class Client extends CustomUser {
    @NotEmpty(message = "Please provide shipping address")
    @Size(max = 255, message = "Shipping address cannot be longer than 255 characters")
    private String shippingAddress;
    private Boolean enabled;

    public Client(Long id, String name, String email, String password, Role role,
                  String shippingAddress, Boolean enabled) {
        super(id, name, email, password, role);
        this.shippingAddress = shippingAddress;
        this.enabled = enabled;
    }
}
