package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.EmployeeNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock employee and Pageable setup
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        pageable = mock(Pageable.class);
    }

    @Test
    void testSaveEmployee() {
        // Mock repository behavior
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Call service method
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Verify results
        assertNotNull(savedEmployee);
        assertEquals(employee.getId(), savedEmployee.getId());
        assertEquals(employee.getName(), savedEmployee.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetEmployeeById_Found() {
        // Mock repository behavior
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Call service method
        Employee foundEmployee = employeeService.getEmployeeById(1L);

        // Verify results
        assertNotNull(foundEmployee);
        assertEquals(employee.getId(), foundEmployee.getId());
        assertEquals(employee.getName(), foundEmployee.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Mock repository behavior
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteEmployeeById() {
        // Call service method
        employeeService.deleteEmployeeById(1L);

        // Verify that repository's deleteById was called
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllEmployees() {
        // Create a Page object with mock employees
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));

        // Mock repository behavior
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        // Call service method
        Page<Employee> result = employeeService.getAllEmployees(pageable);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(pageable);
    }
}
