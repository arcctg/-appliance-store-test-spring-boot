package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Please provide the appliance name")
    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotEmpty(message = "Please provide the model")
    @Size(max = 255, message = "Model cannot be longer than 255 characters")
    private String model;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Manufacturer manufacturer;

    @Enumerated(EnumType.STRING)
    private PowerType powerType;

    @NotEmpty(message = "Please provide the characteristic")
    @Size(max = 255, message = "Characteristic cannot be longer than 255 characters")
    private String characteristic;

    @Size(max = 1000, message = "Description cannot be longer than 1000 characters")
    private String description;

    private Integer power;

    @NotNull(message = "Please provide the price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
