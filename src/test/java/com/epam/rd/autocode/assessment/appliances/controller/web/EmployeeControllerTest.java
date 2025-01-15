package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        // Arrange: Create a mock pageable result
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Employee> mockPage = new PageImpl<>(Collections.emptyList());
        when(employeeService.getAllEmployees(pageable)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/employees"))
                .andExpect(model().attributeExists("pageable"));

        // Verify service method is called
        verify(employeeService, times(1)).getAllEmployees(any(Pageable.class));
    }

    @Test
    void testAddEmployeeForm() throws Exception {
        // Test if the add employee form is displayed correctly
        mockMvc.perform(get("/employees/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/newEmployee"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    void testEditEmployeeForm() throws Exception {
        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(mockEmployee);

        // Test if the edit employee form is displayed correctly
        mockMvc.perform(get("/employees/edit")
                        .param("id", String.valueOf(employeeId)))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/editEmployee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", mockEmployee));

        // Verify service method is called
        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testSaveEmployee() throws Exception {
        Employee mockEmployee = new Employee();

        mockMvc.perform(post("/employees/add-employee")
                        .flashAttr("employee", mockEmployee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService, times(1)).saveEmployee(mockEmployee);
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;
        doNothing().when(employeeService).deleteEmployeeById(employeeId);

        // Test deleting an employee
        mockMvc.perform(get("/employees/delete")
                        .param("id", String.valueOf(employeeId)))
                .andExpect(status().is3xxRedirection());

        // Verify service method is called to delete employee
        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }
}
