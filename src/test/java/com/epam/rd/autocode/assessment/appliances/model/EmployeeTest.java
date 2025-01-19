package com.epam.rd.autocode.assessment.appliances.model;

import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.EMPLOYEE_TYPE;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Employee.CLASS_COUNT_CONSTRUCTORS;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Employee.CLASS_COUNT_FIELDS;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.LONG_TYPE;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.STRING_TYPE;
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

class EmployeeTest {

    private static List<Field> allFields;
    private static List<Constructor<?>> allConstructors;

    private static Class<?> clazz;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        clazz = Class.forName(EMPLOYEE_TYPE);
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

    /*Tests for CONSTRUCTORS*/
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
    @DisplayName(EMPLOYEE_TYPE + " has a default constructor")
    void checkDefaultConstructor() {
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 0)
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName(EMPLOYEE_TYPE + " has constructor with 6 parameters")
    void checkConstructorWithParameter() {
        long count = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6) // Expecting 6 parameters
                .count();
        assertEquals(1, count);
    }


    @Test
    @DisplayName("Check parameter types in constructor with 6 parameters")
    void checkParameterTypeForConstructorWithParameter() {
        final Constructor<?> constructor = allConstructors.stream()
                .filter(c -> c.getParameterCount() == 6) // Expecting 6 parameters
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor with 6 parameters"));

        final List<Parameter> parameters = Arrays.asList(constructor.getParameters());

        // Check for parameter of type Long (id)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(LONG_TYPE))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + LONG_TYPE));

        // Check for parameter of type String (name, email, password, department)
        final long countStringParameters = parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(STRING_TYPE))
                .count();
        assertEquals(4, countStringParameters);

        // Check for parameter of type Role (role)
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(Role.class.getTypeName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with type " + Role.class.getTypeName()));

        // Check for department parameter
        parameters.stream()
                .filter(p -> p.getType().getTypeName().equals(STRING_TYPE) && p.getName().equals("department"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No parameter with name 'department'"));
    }


    /* Tests for FIELDS */
    @Test
    @DisplayName("Check count fields")
    void checkCountFields() {
        assertEquals(CLASS_COUNT_FIELDS, allFields.size());
    }

    @Test
    @DisplayName("Check count private fields")
    void checkAllFieldIsPrivate() {
        final long count = allFields.stream()
                .filter(p -> Modifier.isPrivate(p.getModifiers()))
                .count();
        assertEquals(CLASS_COUNT_FIELDS, count);
    }

    @Test
    @DisplayName("To " + EMPLOYEE_TYPE + " check fields name")
    void checkFieldNameDepartment() {
        final long count = allFields.stream()
                .filter(f -> f.getName().equals("department"))
                .count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Check field type and field name")
    void checkFieldTypeAndName() {
        final long count = allFields.stream()
                .filter(f -> f.getType().getTypeName().equals(STRING_TYPE)
                        && f.getName().equals("department"))
                .count();
        assertEquals(1, count);
    }
}
