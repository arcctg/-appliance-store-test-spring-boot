package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.EmployeeNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final String EMPLOYEE_NAME = "John Doe";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        employee = createTestEmployee();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.updateEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee.getId(), savedEmployee.getId());
        assertEquals(employee.getName(), savedEmployee.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.getEmployeeById(EMPLOYEE_ID);

        assertNotNull(foundEmployee);
        assertEquals(employee.getId(), foundEmployee.getId());
        assertEquals(employee.getName(), foundEmployee.getName());
        verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(EMPLOYEE_ID));
        verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
    }

    @Test
    void testDeleteEmployeeById() {
        employeeService.deleteEmployeeById(EMPLOYEE_ID);

        verify(employeeRepository, times(1)).deleteById(EMPLOYEE_ID);
    }

    @Test
    void testGetAllEmployees() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        Page<Employee> result = employeeService.getAllEmployees(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    private Employee createTestEmployee() {
        Employee employee1 = new Employee();
        employee1.setId(EMPLOYEE_ID);
        employee1.setName(EMPLOYEE_NAME);
        return employee1;
    }
}
