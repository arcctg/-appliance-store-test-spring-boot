package com.epam.rd.autocode.assessment.appliances.model;

import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.CATEGORY_TYPE;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Category.ENUM_CONSTANT_BIG;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Category.ENUM_CONSTANT_SMALL;
import static com.epam.rd.autocode.assessment.appliances.model.TestConstants.Category.ENUM_COUNT_CONSTANTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {
    private static List<?> constants;
    private static Class<?> clazz;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        clazz = Class.forName(CATEGORY_TYPE);
        constants = Arrays.asList(clazz.getEnumConstants());
    }

    @Test
    @DisplayName("Class " + CATEGORY_TYPE + " is Enum")
    void checkIsEnum() {
        final boolean actual = clazz.isEnum();
        assertTrue(actual);
    }

    @Test
    @DisplayName("There must be " + ENUM_COUNT_CONSTANTS + " constants")
    void checkCountConstants() {
        int actual = constants.size();
        assertEquals(ENUM_COUNT_CONSTANTS, actual);
    }

    @Test
    @DisplayName("There must be constant with name " + ENUM_CONSTANT_BIG)
    void checkEnumConstantBIG() {
        long actual = Arrays.stream(clazz.getDeclaredFields())
                .map(f -> f.getName())
                .filter(name -> name.equals(ENUM_CONSTANT_BIG))
                .count();
        assertEquals(1, actual);
    }

    @Test
    @DisplayName("There must be constant with name " + ENUM_CONSTANT_SMALL)
    void checkEnumConstantSMALL() {
        long actual = Arrays.stream(clazz.getDeclaredFields())
                .map(f -> f.getName())
                .filter(name -> name.equals(ENUM_CONSTANT_SMALL))
                .count();
        assertEquals(1, actual);
    }
}