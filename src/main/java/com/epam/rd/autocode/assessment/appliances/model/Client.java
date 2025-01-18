package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a client in the system.
 * Extends the {@link CustomUser} class.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends CustomUser {
    /**
     * The shipping address of the client.
     * Cannot be empty and must be less than 255 characters.
     */
    @NotEmpty(message = "Please provide shipping address")
    @Size(max = 255, message = "Shipping address cannot be longer than 255 characters")
    private String shippingAddress;

    /**
     * Indicates whether the client is enabled.
     * Default value is true.
     * Cannot be null.
     */
    @NotNull(message = "Enabled status cannot be null")
    private Boolean enabled = true;

    /**
     * Constructs a new Client with the specified details.
     *
     * @param id the ID of the client
     * @param name the name of the client
     * @param email the email of the client
     * @param password the password of the client
     * @param role the role of the client
     * @param shippingAddress the shipping address of the client
     * @param enabled whether the client is enabled
     */
    public Client(Long id, String name, String email, String password, Role role,
                  String shippingAddress, Boolean enabled) {
        super(id, name, email, password, role);
        this.shippingAddress = shippingAddress;
        this.enabled = enabled;
    }
}
