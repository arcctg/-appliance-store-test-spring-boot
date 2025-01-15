package com.epam.rd.autocode.assessment.appliances.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.*;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.OrderRow.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRowTest {
    private static List<Field> allFields;
    private static List<Constructor<?>> allConstructors;

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(ORDER_ROW_TYPE);
        allFields = Arrays.asList(clazz.getDeclaredFields());
        allConstructors = Arrays.asList(clazz.getConstructors());
    }

    /*Tests for constructors*/
    @Test
    @DisplayName("Count constructors")
    void checkCountConstructors() {
        assertEquals(CLASS_COUNT_CONSTRUCTORS, allConstructors.size());
    }

    @Test
    @DisplayName("Modifiers constructors can be public")
    void checkModifiersConstructors() {
        boolean actual = allConstructors.stream()
                .allMatch(c -> Modifier.isPublic(c.getModifiers()));
        assertTrue(actual);
    }

    @Test
    @DisplayName("OrderRow has to default constructor")
    void checkDefaultConstructor() {
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 0)
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("OrderRow has constructor with 6 parameters")
    void checkConstructorWithParameter() {
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6) // Expect 6 parameters
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check parameter type in constructor with 6 parameters")
    void checkParameterTypeForConstructorWithParameter() {
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6) // Update to 6 parameters
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with 6 parameters"));

        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        // Check the first parameter type (Long id)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(LONG_TYPE))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + LONG_TYPE));

        // Check the second parameter type (Cart cart)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.Cart"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Cart"));

        // Check the third parameter type (Order order)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.Order"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Order"));

        // Check the fourth parameter type (Appliance appliance)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("com.epam.rd.autocode.assessment.appliances.model.Appliance"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Appliance"));

        // Check the fifth parameter type (Long number)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(LONG_TYPE))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + LONG_TYPE));

        // Check the sixth parameter type (BigDecimal amount)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals("java.math.BigDecimal"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type BigDecimal"));
    }

    @Test
    void checkCountFields() {
        assertEquals(6, allFields.size());  // Expect 6 fields in the OrderRow class
    }


    @Test
    void checkAllFieldsArePrivate() {
        final long count = allFields.stream()
                .filter(p -> Modifier.isPrivate(p.getModifiers()))
                .count();

        assertEquals(6, count);  // Expect 6 private fields
    }


    @ParameterizedTest
    @CsvSource({"id", "cart", "order", "appliance", "number", "amount"})
    @DisplayName("Check all fields are present")
    void checkFieldNames(String name) {
        final long count = allFields.stream()
                .filter(f -> f.getName().equals(name))
                .count();
        assertEquals(1, count);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/OrderRowField.csv")
    void checkFieldTypes(String fieldType, String fieldName) {
        final long count = allFields.stream()
                .filter(f -> f.getType().getTypeName().equals(fieldType)
                        && f.getName().equals(fieldName))
                .count();
        assertEquals(1, count);
    }
}
