package com.epam.rd.autocode.assessment.appliances.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.rd.autocode.assessment.appliances.model.enums.Status;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersTest {

    private static List<Field> allFields;
    private static List<Constructor<?>> allConstructors;

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        final Class<?> userClass = Class.forName("com.epam.rd.autocode.assessment.appliances.model.Order");
        allFields = Arrays.asList(userClass.getDeclaredFields());
        allConstructors = Arrays.asList(userClass.getConstructors());
    }

    @Test
    void checkParameterTypesForConstructor() {
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6)  // Check for 6 parameters
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with 6 parameters"));

        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        parameters.stream()
                .filter(p -> p.getType().equals(Long.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type java.lang.Long"));

        parameters.stream()
                .filter(p -> p.getType().equals(Employee.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Employee"));

        parameters.stream()
                .filter(p -> p.getType().equals(Client.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Client"));

        // Adjust to List compatibility check instead of exact list type check.
        parameters.stream()
                .filter(p -> p.getType().isAssignableFrom(List.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type List<OrderRow>"));

        parameters.stream()
                .filter(p -> p.getType().equals(BigDecimal.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type BigDecimal"));

        parameters.stream()
                .filter(p -> p.getType().equals(Status.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Status"));
    }


    @Test
    void checkCountConstructors() {
        assertEquals(2, allConstructors.size());  // Expect 2 constructors
    }

    @Test
    void checkCountFields() {
        assertEquals(6, allFields.size());  // Expect 6 fields in the Order class
    }

    @Test
    void checkModifiersConstructors() {
        final boolean actual = allConstructors.stream()
                .allMatch(c -> Modifier.isPublic(c.getModifiers()));
        assertTrue(actual);
    }

    @Test
    void checkDefaultConstructor() {
        final long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 0)
                .count();
        assertEquals(1, count);
    }

    @Test
    void checkConstructorWithParameter() {
        final long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6)  // Adjust for constructor parameters
                .count();
        assertEquals(1, count);
    }

    @Test
    void checkParameterTypeForConstructorWithParameter() {
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with 6 parameters"));

        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("java.lang.Long"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type java.lang.Long"));

        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.Client"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Client"));

        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.Employee"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Employee"));

        parameters.stream()
                .filter(p -> p.toString().contains("java.util.List<com.epam.rd.autocode.assessment.appliances.model.OrderRow>"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type java.util.List<com.epam.rd.autocode.assessment.appliances.model.OrderRow>"));

        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("java.math.BigDecimal"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type BigDecimal"));

        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.enums.Status"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Status"));
    }

    @Test
    void checkAllFieldsArePrivate() {
        final boolean isPrivate = allFields.stream()
                .allMatch(f -> Modifier.isPrivate(f.getModifiers()));
        assertTrue(isPrivate);
    }
}
