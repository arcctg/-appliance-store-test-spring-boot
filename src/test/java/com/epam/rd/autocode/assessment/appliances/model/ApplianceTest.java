package com.epam.rd.autocode.assessment.appliances.model;

import com.epam.rd.autocode.assessment.appliances.model.enums.Category;
import com.epam.rd.autocode.assessment.appliances.model.enums.PowerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplianceTest {
    private static List<Field> allFields;
    private static List<Constructor<?>> allConstructors;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName(TestConstants.APPLIANCE_TYPE);
        allFields = Arrays.asList(clazz.getDeclaredFields());
        allConstructors = Arrays.asList(clazz.getConstructors());
    }

    /* Tests for CONSTRUCTORS */
    @Test
    @DisplayName("Count constructors")
    void checkCountConstructors() {
        Assertions.assertEquals(TestConstants.Appliance.CLASS_COUNT_CONSTRUCTORS, allConstructors.size());
    }

    @Test
    @DisplayName("Modifiers constructors can be public")
    void checkModifiersConstructors() {
        boolean actual = allConstructors.stream()
                .allMatch(constructor -> Modifier.isPublic(constructor.getModifiers()));
        assertTrue(actual);
    }

    @Test
    @DisplayName(TestConstants.Appliance.CLASS_NAME + " has default constructor")
    void checkDefaultConstructor() {
        long count = allConstructors.stream()
                .filter(constructor -> constructor.getParameterCount() == 0)
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName(TestConstants.Appliance.CLASS_NAME + " has constructor with " + TestConstants.Appliance.PARAMETERS_IN_CONSTRUCTOR_WITH_PARAMETERS + " parameters")
    void checkConstructorWithParameter() {
        long count = allConstructors.stream()
                .filter(constructor -> constructor.getParameterCount() == TestConstants.Appliance.PARAMETERS_IN_CONSTRUCTOR_WITH_PARAMETERS)
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check parameter type in constructor with parameter")
    void checkParameterTypeForConstructorWithParameter() {
        // Find the constructor with parameters
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == TestConstants.Appliance.PARAMETERS_IN_CONSTRUCTOR_WITH_PARAMETERS)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with two parameters"));

        // Get all constructor parameters
        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        // Check for each type manually to make sure everything is reflected correctly
        parameters.stream()
                .filter(p -> p.getType().equals(Long.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + TestConstants.LONG_TYPE));

        parameters.stream()
                .filter(p -> p.getType().equals(String.class))
                .count(); // Ensure string parameters

        parameters.stream()
                .filter(p -> p.getType().equals(Category.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + TestConstants.CLASS_PACKAGE + "." + TestConstants.Category.ENUM_NAME));

        parameters.stream()
                .filter(p -> p.getType().equals(Manufacturer.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + TestConstants.CLASS_PACKAGE + "." + TestConstants.Manufacturer.CLASS_NAME));

        parameters.stream()
                .filter(p -> p.getType().equals(PowerType.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + TestConstants.CLASS_PACKAGE + "." + TestConstants.PowerType.ENUM_NAME));

        parameters.stream()
                .filter(p -> p.getType().equals(Integer.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + TestConstants.INT_TYPE));

        assertEquals(4, parameters.stream().filter(p -> p.getType().equals(String.class)).count());
    }


    /* Tests for FIELDS */
    @Test
    @DisplayName("Count fields")
    void checkCountFields() {
        Assertions.assertEquals(TestConstants.Appliance.CLASS_COUNT_FIELDS, allFields.size());
    }

    @Test
    @DisplayName("Modifiers fields can be private")
    void checkModifiersFields() {
        long count = allFields.stream()
                .filter(f -> Modifier.isPrivate(f.getModifiers()))
                .count();

        Assertions.assertEquals(TestConstants.Appliance.CLASS_COUNT_FIELDS, count);
    }

    @ParameterizedTest
    @CsvSource({"id,1",
            "name,1",
            "category,1",
            "model,1",
            "manufacturer,1",
            "powerType,1",
            "characteristic,1",
            "description,1",
            "power,1",
            "price,1"
    })
    @DisplayName("To " + TestConstants.Appliance.CLASS_NAME + " check fields name")
    void checkFieldNameName(String name, long expected) {
        final long count = allFields.stream()
                .filter(f -> f.getName().equals(name))
                .count();
        assertEquals(expected, count);
    }

    /*not for student*/
    @DisplayName("Check field type and field name")
    @ParameterizedTest
    @CsvFileSource(resources = "/ApplianceFields.csv")
    void checkNameFieldType(String fieldType, String fieldName, long expected) {
        final long countLong = allFields.stream()
                .filter(f -> f.getType().getTypeName().equals(fieldType)
                        & f.getName().equals(fieldName))
                .count();
        assertEquals(expected, countLong);
    }
}
