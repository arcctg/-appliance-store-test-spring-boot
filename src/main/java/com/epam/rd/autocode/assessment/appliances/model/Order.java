package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderRow> orderRowList = new ArrayList<>();

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public String getAppliancesModels() {
        return orderRowList.stream()
                .map(orderRow -> orderRow.getAppliance().getModel())
                .collect(Collectors.joining(", "));
    }
}
