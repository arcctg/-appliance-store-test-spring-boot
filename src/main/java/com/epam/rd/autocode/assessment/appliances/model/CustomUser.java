package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the system.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUser {

    /**
     * The unique identifier for the user.
     * Generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the user.
     * Cannot be empty and must be less than 100 characters.
     */
    @NotEmpty(message = "Please provide your name")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    /**
     * The email of the user.
     * Cannot be empty and must be a valid email format.
     */
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please provide a valid e-mail")
    private String email;

    /**
     * The password of the user.
     * Cannot be empty and must be at least 3 characters long.
     */
    @NotEmpty(message = "Please provide your password")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    /**
     * The role of the user.
     * Stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}