package com.epam.rd.autocode.assessment.appliances.model;

import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.CLIENT_TYPE;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Manufacturer.CLASS_COUNT_CONSTRUCTORS;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Manufacturer.CLASS_COUNT_FIELDS;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.USER_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientTest {

    private static List<Field> allFields;
    private static List<Constructor<?>> allConstructors;
    private static Class<?> clazz;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        clazz = Class.forName(CLIENT_TYPE);
        allFields = Arrays.asList(clazz.getDeclaredFields());
        allConstructors = Arrays.asList(clazz.getConstructors());
    }

    @Test
    @DisplayName("Test superclass is CustomUser")
    void checkSuperclassIsUser() {
        final Class<?> superclass = clazz.getSuperclass();
        final String actual = superclass.getTypeName();
        assertEquals(USER_TYPE, actual);
    }

    /* Tests for CONSTRUCTORS */
    @Test
    @DisplayName("Count constructors have to be " + CLASS_COUNT_CONSTRUCTORS)
    void checkCountConstructors() {
        assertEquals(CLASS_COUNT_CONSTRUCTORS, allConstructors.size());
    }

    @Test
    @DisplayName("Modifier constructors can be public")
    void checkModifiersConstructors() {
        boolean actual = allConstructors.stream()
                .allMatch(c -> Modifier.isPublic(c.getModifiers()));
        assertTrue(actual);
    }

    @Test
    @DisplayName(CLIENT_TYPE + " has default constructor")
    void checkDefaultConstructor() {
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 0)
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check constructor with 7 parameters")
    void checkConstructorWithParameter() {
        // Update to look for constructor with 7 parameters
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 7)  // Looking for a constructor with 7 parameters
                .count();

        assertEquals(1, count);  // Should find exactly one constructor with 7 parameters
    }


    @Test
    @DisplayName("Check parameter type in constructor with parameters")
    void checkParameterTypeForConstructorWithParameter() {
        // Update to look for constructor with 7 parameters
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 7)  // Change to 7 parameters
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with 7 parameters"));

        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        // Check if the constructor contains parameters of the correct types
        parameters.stream()
                .filter(p -> p.getType().equals(Long.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Long"));

        final long countStringParameters = parameters.stream()
                .filter(p -> p.getType().equals(String.class))
                .count();
        assertEquals(4, countStringParameters);  // There should be 4 String parameters

        parameters.stream()
                .filter(p -> p.getType().equals(Role.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Role"));

        parameters.stream()
                .filter(p -> p.getType().equals(Boolean.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type Boolean"));
    }



    /* Tests for FIELDS */
    @Test
    @DisplayName("Check count fields")
    void checkCountFields() {
        assertEquals(CLASS_COUNT_FIELDS, allFields.size());
    }

    @Test
    @DisplayName("Check all fields are private")
    void checkAllFieldsArePrivate() {
        final long count = allFields.stream()
                .filter(f -> Modifier.isPrivate(f.getModifiers()))
                .count();
        assertEquals(CLASS_COUNT_FIELDS, count);
    }

    @Test
    @DisplayName("Check field name: shippingAddress")
    void checkFieldShippingAddressName() {
        final long count = allFields.stream()
                .filter(f -> f.getName().equals("shippingAddress"))
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check field name: enabled")
    void checkFieldEnabledName() {
        final long count = allFields.stream()
                .filter(f -> f.getName().equals("enabled"))
                .count();
        assertEquals(1, count);
    }

    /* Check field types for shippingAddress and enabled */
    @Test
    @DisplayName("Check field type and name for shippingAddress")
    void checkFieldShippingAddressType() {
        final long count = allFields.stream()
                .filter(f -> f.getType().equals(String.class) && f.getName().equals("shippingAddress"))
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check field type and name for enabled")
    void checkFieldEnabledType() {
        final long count = allFields.stream()
                .filter(f -> f.getType().equals(Boolean.class) && f.getName().equals("enabled"))
                .count();
        assertEquals(1, count);
    }
}
